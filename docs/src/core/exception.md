# 全局异常

捕获和处理应用中的所有异常，减少重复代码，保证接口返回一致性，提高系统可维护性

## 注解

`@GoEnableException`

## 状态码

| 错误码 | 类型        | 说明     | 位置                                       |
| ------ | ----------- | -------- | ------------------------------------------ |
| 1000   | `RUNTIME`   | 运行错误 | GlobalExceptionAdvice → runtimeException   |
| 1010   | `ASYNC`     | 执行错误 | GlobalExceptionAdvice → runtimeException   |
| 2000   | `REQUEST`   | 请求失败 | GlobalExceptionAdvice → requestException   |
| 2010   | `RETRY`     | 重试失败 | HttpRetryer                                |
| 2020   | `LIMITER`   | 请求限流 | LimiterAspect                              |
| 3000   | `VALIDATOR` | 校验错误 | GlobalExceptionAdvice → validatorException |
| 4000   | `DBASE`     | 数据错误 | GlobalExceptionAdvice → jdbcException      |
| 5000   | `TOKEN`     | 令牌错误 | TokenInterceptor                           |
| 6000   | `SIGN`      | 签名错误 | SignerAspect                               |
| 9999   | `UNKNOWN`   | 未知     | -                                          |

## 示例

### 运行时

手动抛空指针异常时

```java
@GetMapping("runtime")
public void runtime() {
  throw new NullPointerException("手动抛空指针异常");
}
```

```json
{
  "code": 1000,
  "msg": "手动抛空指针异常",
  "status": false
}
```

### 请求时

以 `GET` 方式请求 `POST` 方法，请求方式不匹配时

```java
@PostMapping("request")
public void request() {
  log.info("非post请求,抛 {} 异常", HttpRequestMethodNotSupportedException.class);
}
```

```json
{
  "code": 2000,
  "msg": "Request method 'GET' is not supported",
  "status": false
}
```

### 效验时

传递参数为空时，分`Hibernate`、`Spring` 为 2 种情况

- Hibernate Validator

```java
@GetMapping("hibernate/validator")
public void hibernate_validator(@NotBlank String p) {
  log.info("参数空,抛 {} 异常", ConstraintViolationException.class);
}
```

```json
{
  "code": 3000,
  "data": ["hibernate_validator.p 不能为空"],
  "msg": "Validation Error",
  "status": false
}
```

- Spring Validator

```java
@Data
public class BaseVo {
    @NotBlank
    private String key;
}

@GetMapping("spring/validator")
public void spring_validator(@Validated BaseVo vo) {
  log.info("参数空,抛 {} 异常", BindException.class);
}
```

```json
{
  "code": 3000,
  "data": ["key 不能为空"],
  "msg": "Validation Error",
  "status": false
}
```

### 数据库

> 查询无表

```java
@GetMapping("database")
public void database() {
    log.debug("无此表,抛 {} 异常", SQLSyntaxErrorException.class);
    // 不捕获
    JdbcTemplatePlus.queryForMap("select * from xx_test");
    // 捕获
    JdbcTemplatePlus.get().queryForMap("select * from xx_test", Maps.newConcurrentMap());
}
```

```json
{
  "code": 4000,
  "msg": "PreparedStatementCallback; bad SQL grammar [select * from xx_test]",
  "status": false
}
```

### 自定义

```java
@GetMapping("custom")
public void custom() {
    throw new RunException(RunExc.SIGN);
}
```

```json
{
  "code": 6000,
  "msg": "Signature Error",
  "status": false
}
```

::: warning 注意

- 某些异常会暴露出包名、类名、方法名，请根据实际场景单独处理

```java
@PostMapping("save")
public User save(BaseVo base, Member req, Model model) {
    try {
       // ....
       return user;
    } catch (Exception e) {
       throw new RunException(RunExc.RUNTIME, "保存异常");
    }
}
```

>

```json
{ "code": 1000, "msg": "保存异常", "success": false }
```

:::
