# 网络请求

基于 [`Hutool`](https://www.hutool.cn/docs/#/http/%E6%A6%82%E8%BF%B0) 扩展，优雅的对网络异常进行重试

## 示例

重试 `3` 次，间隔 `1` 秒、限制 `2`秒

```java
public static final String URL = "https://front-gateway.mtime.cn/ticket/schedule/showing/movies.api?locationId=561";
```

### 默认

```java
@Test
@Order(1)
public void request() {
    // 构造请求
    HttpRequest request = HttpRequest.get(URL);
    // 请求重试
    HttpResponse response = HttpRequests.retry(request);
    // 解析结果
    Object lid = R.eval(response.body(), "$.data.lid");
    // 验证结果
    assertEquals(lid, 561);
}
```

```java
@GetMapping("request")
public Result request() {
    // 构造请求
    HttpRequest request = HttpRequest.get(URL);
    // 请求重试
    HttpResponse response = HttpRequests.retry(request);
    // 解析结果
    Object lid = R.eval(response.body(), "$.data.lid");
    return R.succ(Dict.create().set("lid", lid));
}
```

### 重试器

```java
@Test
@Order(2)
public void custom() {
    // 构造请求
    HttpRequest request = HttpRequest.get(URL);
    // 自定义重试器
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
    // 请求重试
    HttpResponse response = HttpRequests.retry(request, retry);
    // 解析结果
    Object lid = R.eval(response.body(), "$.data.lid");
    // 验证结果
    assertEquals(lid, 561);
}
```

```log
2025-10-14 16:45:40 WARN http-nio-8080-exec-1:393215 HttpRetryer.java:54 - retry:1 delay:2013 exception cause by:java.util.concurrent.TimeoutException
2025-10-14 16:45:43 WARN http-nio-8080-exec-1:396245 HttpRetryer.java:54 - retry:2 delay:5042 exception cause by:java.util.concurrent.TimeoutException
2025-10-14 16:45:47 WARN http-nio-8080-exec-1:399270 HttpRetryer.java:54 - retry:3 delay:8068 exception cause by:java.util.concurrent.TimeoutException
```

#### 成功

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "lid": 561
  },
  "status": true
}
```

#### 失败

```json
{
  "code": 2000,
  "msg": "Request Failed: http retry error",
  "status": false
}
```
