# 网络请求

请求网络重试

## 示例

### 默认

```java
@Test
public void http1() {
    // 构造请求
    RequestBuilder req = Requests.post(url).params(Parameter.of("k1", "v1"));
    // 请求重试
    RawResponse response = RequestsHelper.retry(req);
    // 获取结果
    TestRetryResponse result = response.readToJson(TestRetryResponse.class);
}
```

### 自定义

```java
@Test
public void http2() {
    // 构造请求
    RequestBuilder builder = HttpHelper.post(url).params(Parameter.of("k1", "v1"));
    // 构造重试（见下章）
    Retryer<RawResponse> retryer = RetryerBuilder.<RawResponse>newBuilder().build();
    // 请求
    RawResponse response = HttpHelper.retry(builder, retryer);
    // 获取结果
    TestRetryResponse result = response.readToJson(TestRetryResponse.class);
}
```

### 重试验证

这里默认重试了 3 次，每次等 1 秒

```
[FastBoot][ WARN][08-11 14:09:15]-->[http-nio-9090-exec-9:201147][onRetry(HttpRetryer.java:76)] | - onRetry number:1 error:false result:true statusCode:404 delay:78
[FastBoot][ WARN][08-11 14:09:16]-->[http-nio-9090-exec-9:202222][onRetry(HttpRetryer.java:76)] | - onRetry number:2 error:false result:true statusCode:404 delay:1153
[FastBoot][ WARN][08-11 14:09:17]-->[http-nio-9090-exec-9:203302][onRetry(HttpRetryer.java:76)] | - onRetry number:3 error:false result:true statusCode:404 delay:2234
[FastBoot][ERROR][08-11 14:09:17]-->[http-nio-9090-exec-9:203303][runException(GlobalExceptionAdvice.java:134)] | - runException ......
com.xiesx.FastBoot.core.exception.RunException: 请求错误:http retry error
	at com.xiesx.FastBoot.support.request.HttpRetryer.retry(HttpRetryer.java:92) ~[classes/:?]
```

#### 重试成功

```
{
	"code":0,
	"msg": "success",
	"data":{
	},
	"success":true
}
```

#### 重试失败

```
{
    "code": 2000,
    "msg": "请求失败:http retry error",
    "success": false
}
```
