# 重试机制

该功能由 [`Guava-Retrying`](https://github.com/rholder/guava-retrying) 源改而来

> 在很多业务场景中，为了排除系统中的各种不稳定因素，以及逻辑上的错误，并最大概率保证获得预期的结果，重试机制都是必不可少的。

> 尤其是调用远程服务，在高并发场景下，很可能因为服务器响应延迟或者网络原因，造成我们得不到想要的结果，或者根本得不到响应。这个时候，一个优雅的重试调用机制，可以让我们更大概率保证得到预期的响应。

## 如何优雅地设计重试实现

1. 什么条件下重试
2. 什么条件下停止
3. 如何停止重试
4. 停止重试等待多久
5. 如何等待
6. 请求时间限制
7. 如何结束
8. 如何监听整个重试过程

## 构造重试

网络重试为例：

```java
Retryer<RawResponse> retry = RetryerBuilder.<RawResponse>newBuilder()
    // 重试条件
    .retryIfException()
    .retryIfResult(reRetryPredicate)
    // 等待策略：请求间隔1s
    .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
    // 停止策略：尝试请求3次
    .withStopStrategy(StopStrategies.stopAfterAttempt(3))
    // 时间限制：请求限制2s
    .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))
    // 重试监听
    .withRetryListener(reRetryListener)
    //
    .build();
```

如当返回不等于 200 时重试

```java
protected Predicate<RawResponse> reRetryPredicate = raw -> {
   return raw.statusCode() != 200;
};
```

如当返回 result 为空时、当返回值=-3 时（统一返回章节中-3=重试）

```java
protected Predicate<Result> reRetryPredicate = result -> {
   if (ObjectUtils.isEmpty(result)) {
      return true;
   } else if (result.getCode() == -3) {
      return true;
   }
   return false;
};
```

## 示例

```java
@Test
public void retry() {
    // 构造重试
    Retryer<Result> retry = RetryerBuilder.<Result>newBuilder()
            // 重试条件
            .retryIfException()
            // 返回指定结果时重试
            .retryIfResult((@Nullable Result result) -> {
                if (ObjectUtils.isEmpty(result)) {
                    return true;
                } else if (result.getCode() == -3) {
                    return true;
                }
                return false;
            })
            // 等待策略：每次请求间隔1s
            .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
            // 停止策略 : 尝试请求2次
            .withStopStrategy(StopStrategies.stopAfterAttempt(2))
            // 时间限制 : 请求限制2s
            .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(5, TimeUnit.SECONDS))
            .withRetryListener(new RetryListener() {

                @Override
                public <V> void onRetry(Attempt<V> attempt) {
                    long number = attempt.getAttemptNumber();
                    long delay = attempt.getDelaySinceFirstAttempt();
                    boolean isError = attempt.hasException();
                    boolean isResult = attempt.hasResult();
                    if (attempt.hasException()) {
                        if (attempt.getExceptionCause().getCause() instanceof RunException) {
                            RunException runException = (RunException) attempt.getExceptionCause().getCause();
                            log.warn("onException causeBy:{} {}", runException.getErrorCode(), runException.getMessage());
                        } else {
                            log.warn("onException causeBy:{}", attempt.getExceptionCause().toString());
                        }
                    } else {
                        if (attempt.hasResult()) {
                            try {
                                V result = attempt.get();
                                if (result instanceof Result) {
                                    log.warn("onRetry number:{} error:{} result:{} statusCode:{} delay:{}", number, isError, isResult,
                                            ((Result) result).getCode(), delay);
                                }
                            } catch (ExecutionException e) {
                                log.error("onResult exception:{}", e.getCause().toString());
                                throw new RunException(RunExc.RETRY, "test retry");
                            }
                        }
                    }
                }
            })
            .build();

    try {
        Result result = retry.call(() -> {
            // 构造请求
            RequestBuilder req = Requests.post(url).params(Parameter.of("configKey", "appLaunch"));
            // 请求重试
            RawResponse response = RequestsHelper.retry(req);
            // 获取结果
            TestRetryResponse result1 = response.readToJson(TestRetryResponse.class);
            // 验证结果，如果结果正确则返回，错误则重试
            if (result1.getCode() == 0) {
                return R.succ(result1.getData());
            } else {
                return R.fail(Result.RETRY, result1.getMsg());
            }
        });
        // 验证结果，如果结果正确则返回，错误则重试
        log.info(JSON.toJSONString(R.succ(result.getData())));
    } catch (ExecutionException | RetryException e) {
        throw new RunException(RunExc.RETRY, "test retry");
    }
}
```

## 验证

述示例代码可知，远程请求调用，对业务、网络均模拟重试操作。这类是比较常见的业务场景。在某些特殊场景下需要对某些请求、业务需要做重试。如：
网络 A：重试 3 次，每次等待 1 秒，限制 2 秒
业务 B：重试 2 次，每次等待 1 秒，限制 5 秒

### 当网络 A 异常时：

```
[FastBoot][ WARN][08-11 14:23:53]-->[pool-6-thread-1:1079595][onRetry(HttpRetryer.java:76)] | - onRetry number:1 error:false result:true statusCode:404 delay:180
[FastBoot][ WARN][08-11 14:23:54]-->[pool-6-thread-1:1080701][onRetry(HttpRetryer.java:76)] | - onRetry number:2 error:false result:true statusCode:404 delay:1285
[FastBoot][ WARN][08-11 14:23:55]-->[pool-6-thread-1:1081792][onRetry(HttpRetryer.java:76)] | - onRetry number:3 error:false result:true statusCode:404 delay:2375
[FastBoot][ WARN][08-11 14:23:55]-->[http-nio-9090-exec-10:1081793][onRetry(ApIController.java:129)] | - onException causeBy:2000 请求错误:http retry error
[FastBoot][ WARN][08-11 14:23:56]-->[pool-6-thread-1:1082901][onRetry(HttpRetryer.java:76)] | - onRetry number:1 error:false result:true statusCode:404 delay:98
[FastBoot][ WARN][08-11 14:23:58]-->[pool-6-thread-1:1084004][onRetry(HttpRetryer.java:76)] | - onRetry number:2 error:false result:true statusCode:404 delay:1200
[FastBoot][ WARN][08-11 14:23:59]-->[pool-6-thread-1:1085138][onRetry(HttpRetryer.java:76)] | - onRetry number:3 error:false result:true statusCode:404 delay:2334
[FastBoot][ WARN][08-11 14:23:59]-->[http-nio-9090-exec-10:1085139][onRetry(ApIController.java:129)] | - onException causeBy:2000 请求错误:http retry error
[FastBoot][ERROR][08-11 14:23:59]-->[http-nio-9090-exec-10:1085143][runException(GlobalExceptionAdvice.java:134)] | - runException ......
com.xiesx.FastBoot.core.exception.RunException: 重试失败:test retry error
```

### 当网络 A 异常时，业务 B 限制 2 秒时（注意：这里只打印 2 个，因为 B 做了限制 2 秒，A 会重试 1 秒\*3 次，超出 B 限制）

```
[FastBoot][ WARN][08-11 14:23:53]-->[pool-6-thread-1:1079595][onRetry(HttpRetryer.java:76)] | - onRetry number:1 error:false result:true statusCode:404 delay:180
[FastBoot][ WARN][08-11 14:23:54]-->[pool-6-thread-1:1080701][onRetry(HttpRetryer.java:76)] | - onRetry number:2 error:false result:true statusCode:404 delay:1285
[FastBoot][ WARN][08-11 14:23:55]-->[http-nio-9090-exec-10:1081793][onRetry(ApIController.java:129)] | - onException causeBy:2000 请求错误:http retry error
[FastBoot][ WARN][08-11 14:23:56]-->[pool-6-thread-1:1082901][onRetry(HttpRetryer.java:76)] | - onRetry number:1 error:false result:true statusCode:404 delay:98
[FastBoot][ WARN][08-11 14:23:58]-->[pool-6-thread-1:1084004][onRetry(HttpRetryer.java:76)] | - onRetry number:2 error:false result:true statusCode:404 delay:1200
[FastBoot][ WARN][08-11 14:23:59]-->[http-nio-9090-exec-10:1085139][onRetry(ApIController.java:129)] | - onException causeBy:2000 请求错误:http retry error
[FastBoot][ERROR][08-11 14:23:59]-->[http-nio-9090-exec-10:1085143][runException(GlobalExceptionAdvice.java:134)] | - runException ......
com.xiesx.FastBoot.core.exception.RunException: 重试失败:test retry error
```

### 网络 A 正常：业务 B 错误（A 重试 1 次、B 重复 2 次)

```
[FastBoot][ WARN][08-11 14:40:18]-->[pool-7-thread-1:2064857][onRetry(HttpRetryer.java:76)] | - onRetry number:1 error:false result:true statusCode:200 delay:1397
[FastBoot][ WARN][08-11 14:40:19]-->[http-nio-9090-exec-4:2065102][onRetry(ApIController.java:138)] | - onRetry number:1 error:false result:true statusCode:-3 delay:1642
[FastBoot][ WARN][08-11 14:40:20]-->[pool-7-thread-1:2066165][onRetry(HttpRetryer.java:76)] | - onRetry number:1 error:false result:true statusCode:200 delay:49
[FastBoot][ WARN][08-11 14:40:20]-->[http-nio-9090-exec-4:2066166][onRetry(ApIController.java:138)] | - onRetry number:2 error:false result:true statusCode:-3 delay:2707
[FastBoot][ERROR][08-11 14:40:20]-->[http-nio-9090-exec-4:2066168][runException(GlobalExceptionAdvice.java:134)] | - runException ......
com.xiesx.FastBoot.core.exception.RunException: 重试失败:test retry error
```

### 网络 A、B 正常 （AB 重复 1 次、即首次)

```
[FastBoot][ WARN][08-11 14:48:38]-->[pool-8-thread-1:2564941][onRetry(HttpRetryer.java:76)] | - onRetry number:1 error:false result:true statusCode:200 delay:159
[FastBoot][ WARN][08-11 14:48:38]-->[http-nio-9090-exec-1:2564946][onRetry(ApIController.java:138)] | - onRetry number:1 error:false result:true statusCode:0 delay:164
```

### 重试成功返回

```
{
	"code":0,
	"msg": "success",
	"data":{
	},
	"success":true
}
```

### 重试失败返回

```
{
    "code": 7000,
    "msg": "重试失败:test retry error",
    "success": false
}
```
