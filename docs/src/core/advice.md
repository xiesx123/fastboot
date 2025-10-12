# 统一返回

消除接口格式差异，提升前后端协作与处理效率，降低维护成本

## 注解

`@GoEnableBody`

## 状态码

| 状态码 | 类型    | 说明     |
| ------ | ------- | -------- |
| 0      | Success | 操作成功 |
| -1     | Fail    | 操作失败 |
| -2     | Error   | 系统异常 |
| -3     | Retry   | 重试失败 |

## 类型

| 类型                              | 处理 |
| --------------------------------- | ---- |
| `java.util.Map`                   | 🚫   |
| `java.lang.Iterable`              | 🚫   |
| `java.lang.String`                | 🚫   |
| `com.xiesx.fastboot.base.IStatus` | 🚫   |
| `java.lang.Object`                | ✅   |

## 格式

- 对象

```json
{ "code": 0, "msg": "success", "data": {}, "status": true }
```

- 数组

```json
{ "code": 0, "msg": "success", "data": [], "status": true }
```

- 分页

```json
{ "code": 0, "msg": "success", "data": [], "status": true, "count": 32 }
```

## 显式

- 对象/数组

```java
@GetMapping("xxx")
public Result getXXX() {
    // return R.succ();
    // return R.succ(Object data)
    // return R.succ(String msg)
    // return R.succ(String msg, Object data)
    // return R.succ(Integer code, String msg)
    // return R.succ(Integer code, String msg, Object data)
    // return R.fail();
    // return ...
    // return R.error();
    // return ...
}
```

- 分页

```java
@GetMapping("xxx")
public RResult pageXXX() {
    // return PR.create(Page<?> page)
    // return PR.create(List<?> data)
    // return PR.create(List<?> data,Integer total)
}
```

## 忽略

- 通过配置文件

```yml
fastboot:
  advice:
    body-ignores-urls:
      - /swagger-resources,/api-docs
      - /body/ignore
```

- 通过注解方式

```java
@RestController
@RequestMapping("body")
public class BodyController {

  @IgnoreBody
  @GetMapping("ignore")
  public MockUser ignore() {
      return MockData.user();
  }
}
```

```json
{
  "name": "张三",
  "birthDay": "2025-10-07 17:29:03",
  "registerDay": "2025-10-07 17:29:03",
  "idCard": "51343620000320711X",
  "phone": "09127518479",
  "tel": "13800138000",
  "address": "xx市xx区xxxx街道xxx号",
  "email": "123456789@qq.com",
  "password": "0lcWdPLC",
  "carnumber": "京A88888",
  "status": "A",
  "balance": "100.00",
  "enable": false
}
```

## 示例

### Map

```java
@GetMapping("map")
public Map<String, Object> map() {
    return MockData.map();
}
```

```json
{
  "k1": "1",
  "k2": 2,
  "k3": "3",
  "k4": 4.1,
  "k5": 5.2,
  "k6": true,
  "k7": "7",
  "k8": "2021-06-17 16:58:53"
}
```

### Iterable

```java
@GetMapping("list")
public List<String> list() {
    return MockData.list();
}
```

```json
["k1", "k2", "k3", "k4", "k5", "k6", "k7", "k8"]
```

### String

```java
@GetMapping("string")
public String string() {
  return Configed.FASTBOOT;
}
```

```text
fastboot
```

### Result

```java
@GetMapping("result")
public Result result() {
    return R.succ(MockData.map());
}
```

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "k1": "1",
    "k2": 2,
    "k3": "3",
    "k4": 4.1,
    "k5": 5.2,
    "k6": true,
    "k7": "7",
    "k8": "2021-06-17 16:44:50"
  },
  "status": true
}
```

### PResult

```java
@GetMapping("page")
public PResult page(PageVo vo) {
  return PR.create(mLogRecordRepository.findAll(PageRequest.of(vo.getPage(), vo.getSize())));
}
```

```json
{
  "code": 0,
  "msg": "success",
  "data": [
    {
      "id": 1975440458159095808,
      "createDate": "2025-10-07 13:58:01",
      "updateDate": "2025-10-07 13:58:01"
      // ...
    },
    {
      "id": 1975440458662412288,
      "createDate": "2025-10-07 13:58:01",
      "updateDate": "2025-10-07 13:58:01"
      // ...
    }
  ],
  "count": 48,
  "status": true
}
```
