# 令牌认证

xxx

## 注解

- `@GoEnableToken`
- `@GoHeader`
- `@GoToken`

## 配置

```yml
fastboot:             # fastboot
  token:              # ======= 令牌认证
    header: token     # 令牌键，默认：token
    include-paths:    # 包含路径
      - /api/**
      - /token/**
    exclude-paths:    # 排除路径
      - /js/**
```

## 令牌

> 生成令牌，负载包含`uid`，

```java
public static void main(String[] args) {
    Map<String, Object> headers = Maps.newConcurrentMap();
    headers.put("subscribe", "free");
    Map<String, Object> payload = Maps.newConcurrentMap();
    payload.put(TokenCfg.UID, "1");
    String token =JwtHelper.simple("fastboot", "api", headers, payload, JwtHelper.JWT_EXPIRE_D_1);
    Console.log(token);

    JWT jwt = JwtHelper.parser(token);
    Console.log("签名算法：" + jwt.getSigner().getAlgorithm());
    JWTHeader jh = jwt.getHeader();
    Console.log("头部信息：" + jh.getClaimsJson());
    JWTPayload jp = jwt.getPayload();
    Console.log("负载信息：" + jp.getClaimsJson());
}
```

```
eyJzdWJzY3JpYmUiOiJmcmVlIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJhYTJlOGU1Zjg3Yzg0NGRiOGQ1ZTYzZDM4NzhlNzI3NiIsInN1YiI6ImZhc3Rib290IiwiaXNzIjoiZmFzdGJvb3QiLCJpYXQiOjE3NjAxNjY0NjYsImV4cCI6MTc2MDI1Mjg2NiwibmJmIjoxNzYwMTY2NDY2LCJ1aWQiOiIxIiwiYXVkIjpbImFwaSJdfQ.rD19SWAp9CBW08IDK1se_DvlI8kyT8Xkanp534Jvb_o
签名算法：HmacSHA256
头部信息：{"subscribe":"free","typ":"JWT","alg":"HS256"}
负载信息：{"jti":"aa2e8e5f87c844db8d5e63d3878e7276","sub":"fastboot","iss":"fastboot","iat":1760166466,"exp":1760252866,"nbf":1760166466,"uid":"1","aud":["api"]}
```

## 示例

```java
@RestController
@RequestMapping("token")
public class TokenController {

    @GetMapping("header")
    public Result header(String name, @GoToken String uid, @GoHeader TokenVo header, TokenVo vo) {
        JSONObject headers = JwtHelper.parser(header.token).getHeaders();
        String subscribe = headers.getStr("subscribe", StrUtil.EMPTY);
        return R.succ(Lists.newArrayList(name, uid, subscribe, header, vo));
    }
}
```

```
[FastBoot][ INFO][06-27 02:21:37]-->[http-nio-auto-1-exec-1:19205][loggerAroundAspect(LoggerAspect.java:109)] | - | request header | ["fasotboot","123",{"uid":"123"}]
[FastBoot][ INFO][06-27 02:21:37]-->[http-nio-auto-1-exec-1:19223][loggerAroundAspect(LoggerAspect.java:120)] | - | response time 9ms | header | {"code":0,"msg": "success","data":["fasotboot","123",{"uid":"123"}],"status":true}
```

### 不传

```json
{
  "code": 5000,
  "msg": "Token Error: 未登录",
  "status": false
}
```

### 过期
```json
{
  "code": 5000,
  "msg": "Token Error: 已过期",
  "status": false
}
```

### 传错

```json
{
  "code": 5000,
  "msg": "Token Error: xxxxx",
  "status": false
}
```

### 正确
```json
{
  "code": 0,
  "data": [
    "fastboot",
    "1",
    "free",
    {
      "h1": "1",
      "token": "eyJzdWJzY3JpYmUiOiJmcmVlIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJjZWM2Yjc2ZWU3M2Y0ODRkYjdlNTVlZThiMTM4NTBhOCIsInN1YiI6ImZhc3Rib290IiwiaXNzIjoiZmFzdGJvb3QiLCJpYXQiOjE3NjAxNjM4MDEsImV4cCI6MTc2MDI1MDIwMSwibmJmIjoxNzYwMTYzODAxLCJ1aWQiOiIxIiwiYXVkIjpbImFwaSJdfQ.J8VbmYDJ8gh3lkNmGYboorU6Wl17A3v-Hi_3IelfUps"
    },
    {
      "h1": "1",
      "token": "eyJzdWJzY3JpYmUiOiJmcmVlIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJjZWM2Yjc2ZWU3M2Y0ODRkYjdlNTVlZThiMTM4NTBhOCIsInN1YiI6ImZhc3Rib290IiwiaXNzIjoiZmFzdGJvb3QiLCJpYXQiOjE3NjAxNjM4MDEsImV4cCI6MTc2MDI1MDIwMSwibmJmIjoxNzYwMTYzODAxLCJ1aWQiOiIxIiwiYXVkIjpbImFwaSJdfQ.J8VbmYDJ8gh3lkNmGYboorU6Wl17A3v-Hi_3IelfUps"
    }
  ],
  "msg": "success",
  "status": true
}
```
