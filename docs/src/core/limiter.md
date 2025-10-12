# 请求限流

通过限制单位时间内对接口或资源的访问次数，防止系统过载、保障服务稳定性

## 注解

- `@GoEnableLimiter`
- `@GoLimit`

## 参数

:::tip `@GoLimit`

- limit ： 每秒限流 n 个请求；
- message ：返回数据 msg 信息
  :::

## 示例

::: code-group

```java [Controller.java]
@GoLimiter(limit = 1, message = "该接口测试每秒内限流1个请求")
@RequestMapping("limit")
public Result limit() {
    return R.succ(DateUtil.now());
}
```

:::

### 正常

```json
{
  "code": 0,
  "msg": "2025-10-07 18:15:33",
  "status": true
}
```

### 限流

```json
{
  "code": 2020,
  "msg": "Request Rate Limited: 该接口测试每秒内限流1个请求",
  "status": false
}
```
