# 重试机制

基于 [`Guava-Retrying`](https://github.com/rholder/guava-retrying) 源码修改

在很多业务场景中，为了排除系统中的各种不稳定因素，以及逻辑上的错误，并最大概率保证获得预期的结果，重试机制都是必不可少的。
尤其是调用远程服务，在高并发场景下，很可能因为服务器响应延迟或者网络原因，造成我们得不到想要的结果，或者根本得不到响应。这个时候，一个优雅的重试调用机制，可以让我们更大概率保证得到预期的响应。

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

### 网络请求 `HttpResponse`

- 当请求异常时重试
- 当响应状态码范围在 200~299 内

```java
Retryer<HttpResponse> retry = RetryerBuilder.<HttpResponse>newBuilder()
        // 重试条件
        // 1: 当请求异常时重试
        .retryIfException()
        // 2: 状态码范围在200~299内
        .retryIfResult(response -> !response.isOk())
        // 等待策略：请求间隔1s
        .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
        // 停止策略：尝试请求3次
        .withStopStrategy(StopStrategies.stopAfterAttempt(3))
        // 时间限制：请求限制2s
        .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))
        // 重试监听
        .withRetryListener(HttpRetryer.reRetryListener)
        .build();
```

### 统一返回 `Result`

- 当发生异常时重试
- 当出现指定结果时重试，(返回 result 为空时 || 返回 `code=-1` 时)

```java
Retryer<HttpResponse> retryA = RetryerBuilder.<HttpResponse>newBuilder()
        // 重试条件
        // 1: 当发生异常时重试
        .retryIfException()
        // 2: 当指定结果时重试，(返回 result 为空时 || 返回 `code=-1` 时)
        .retryIfResult((Result result) -> ObjectUtil.isNull(result) || result.code() == -1)
        // 等待策略：请求间隔1s
        .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
        // 停止策略：尝试请求3次
        .withStopStrategy(StopStrategies.stopAfterAttempt(3))
        // 时间限制：请求限制2s
        .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))
        // 重试监听
        .withRetryListener(HttpRetryer.reRetryListener)
        .build();
```

## 示例

- 网络请求重试：retryA：重试 `3` 次，间隔 `1` 秒，限制 `2` 秒
- 业务处理重试：retryB：重试 `2` 次，间隔 `1` 秒，限制 `5` 秒

```java
@Test
@Order(1)
public void retry() {
    // 业务处理重试
    Retryer<Result> retryB = RetryerBuilder.<Result>newBuilder()
            // 重试条件
            .retryIfException()
            // 当出现指定结果时重试，(返回 result 为空时 || 返回 `code=-1` 时)
            .retryIfResult((Result result) -> ObjectUtil.isNull(result) || result.code() == -1)
            // 等待策略：请求间隔1s
            .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
            // 停止策略：尝试请求2次
            .withStopStrategy(StopStrategies.stopAfterAttempt(2))
            // 时间限制：请求限制5s
            .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))
            // 数据监听
            .withRetryListener(HttpRetryer.reRetryListener)
            .build();
    try {
        Result result = retryB.call(() -> {
                // 构造请求
                HttpRequest req = HttpRequest.get(URL);
                // 网络请求重试(retryA)
                HttpResponse response = HttpRequests.retry(req);
                // 获取结果
                String body = response.body();
                // 手动限制2秒
                // ThreadUtil.safeSleep(2000);
                // 解析结果
                Object code = R.eval(body, "$.code");
                String msg = R.eval(body, "$.msg").toString();
                // 验证结果
                if (Objects.equal(code, 0)) {
                    return R.succ(msg, R.eval(body, "$.data"));
                }
                return R.fail(msg);
        log.info(R.eval(result.data(), "$.lid"));
    } catch (ExecutionException e) {
        throw new RunException(RunExc.RUNTIME, "run");
    } catch (RetryException e) {
        throw new RunException(RunExc.RETRY, "retry");
    }
}
```

### 当网络 A 异常时

```
2025-10-14 17:33:31 WARN pool-3-thread-1:4147 HttpRetryer.java:54 - retry:1 delay:73 exception cause cn.hutool.core.io.IORuntimeException: UnknownHostException: front-gateway.mtime.cn
2025-10-14 17:33:32 WARN pool-3-thread-1:5148 HttpRetryer.java:54 - retry:2 delay:1075 exception cause cn.hutool.core.io.IORuntimeException: UnknownHostException: front-gateway.mtime.cn
2025-10-14 17:33:33 WARN main:6064 HttpRetryer.java:54 - retry:1 delay:2000 exception cause by:java.util.concurrent.TimeoutException
2025-10-14 17:33:34 WARN pool-3-thread-1:7067 HttpRetryer.java:54 - retry:1 delay:0 exception cause cn.hutool.core.io.IORuntimeException: UnknownHostException: front-gateway.mtime.cn
2025-10-14 17:33:35 WARN pool-3-thread-1:8068 HttpRetryer.java:54 - retry:2 delay:1002 exception cause cn.hutool.core.io.IORuntimeException: UnknownHostException: front-gateway.mtime.cn
2025-10-14 17:33:36 WARN main:9066 HttpRetryer.java:54 - retry:2 delay:5002 exception cause by:java.util.concurrent.TimeoutException
2025-10-14 17:33:37 WARN pool-3-thread-1:10067 HttpRetryer.java:54 - retry:1 delay:0 exception cause cn.hutool.core.io.IORuntimeException: UnknownHostException: front-gateway.mtime.cn
2025-10-14 17:33:38 WARN pool-3-thread-1:11069 HttpRetryer.java:54 - retry:2 delay:1002 exception cause cn.hutool.core.io.IORuntimeException: UnknownHostException: front-gateway.mtime.cn
2025-10-14 17:33:39 WARN main:12068 HttpRetryer.java:54 - retry:3 delay:8004 exception cause by:java.util.concurrent.TimeoutException
```

### 当网络 A 正常、业务 B 限制 2 秒时

```
2025-10-14 17:43:11 WARN main:6026 HttpRetryer.java:54 -  retry:1 delay:2001 exception cause by:java.util.concurrent.TimeoutException
2025-10-14 17:43:14 WARN main:9029 HttpRetryer.java:54 -  retry:2 delay:5004 exception cause by:java.util.concurrent.TimeoutException
2025-10-14 17:43:17 WARN main:12031 HttpRetryer.java:54 - retry:3 delay:8006 exception cause by:java.util.concurrent.TimeoutException
```

### 网络 A/B 正常

```
2025-10-14 17:49:42 INFO main:4804 RetryerTest.java:79 - 561
```
