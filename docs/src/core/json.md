# 数据转换

替换默认 `Jackson` 使用 `FastJson2` 以增强 `JSON` 序列化与反序列化功能

## 注解

- `@GoEnableFastJson`
- `@GoDesensitized`

## 配置

```yml
fastboot:                                 # fastboot
  fastjson:                               # ======= 数据转换（fastjson实现）
    supported-media-types:                # 支持媒体类型，默认：text/html、application/json
      - text/html;charset=UTF-8
      - application/json;charset=UTF-8
    config:                               # 配置
      charset: utf-8                      # 编码格式，默认：UTF-8
      date-format: yyyy-MM-dd HH:mm:ss    # 日期格式，默认：yyyy-MM-dd HH:mm:ss
      writer-features:                    # 序列化
        - PrettyFormat                    # 格式化输出
        - WriteNullBooleanAsFalse         # 布尔类型如果为null，输出为false，而不是null
        - WriteEnumUsingToString          # 枚举类型用ToString输出
      reader-features:
        - UseDoubleForDecimals
      ...
    desensitize: true                     # 启用脱敏，默认：false
```

## 脱敏

如需使用脱敏，字段启用 `@GoDesensitized(type = xxx)` ，类型如下

| 类型         | 描述    |
| ------------ | ------- |
| USER_ID      | 用户 id |
| CHINESE_NAME | 中文名  |
| ID_CARD      | 身份证  |
| FIXED_PHONE  | 座机号  |
| MOBILE_PHONE | 手机号  |
| ADDRESS      | 地址    |
| EMAIL        | 邮件    |
| PASSWORD     | 车牌    |
| BANK_CARD    | 银行卡  |

## 示例

### 定义

::: code-group

```java [User.java]
@Data
@Accessors(chain = true)
public class MockUser {

    @JSONField(ordinal = 1)
    @GoDesensitized(type = DesensitizedType.CHINESE_NAME)
    private String name;

    @JSONField(ordinal = 2)
    private DateTime birthDay;

    @JSONField(ordinal = 3, format = "yyyy-MM-dd")
    private DateTime registerDay;

    @JSONField(ordinal = 4)
    @GoDesensitized(type = DesensitizedType.ID_CARD)
    private String idCard;

    @JSONField(ordinal = 5)
    @GoDesensitized(type = DesensitizedType.FIXED_PHONE)
    private String phone;

    @JSONField(ordinal = 6)
    @GoDesensitized(type = DesensitizedType.MOBILE_PHONE)
    private String tel;

    @JSONField(ordinal = 7)
    @GoDesensitized(type = DesensitizedType.ADDRESS)
    private String address;

    @JSONField(ordinal = 8)
    @GoDesensitized(type = DesensitizedType.EMAIL)
    private String email;

    @JSONField(ordinal = 9)
    @GoDesensitized(type = DesensitizedType.PASSWORD)
    private String password;

    @JSONField(ordinal = 10)
    @GoDesensitized(type = DesensitizedType.CAR_LICENSE)
    private String carnumber;

    @JSONField(ordinal = 11)
    private StatusEnum status;

    @JSONField(ordinal = 12, format = "0.00")
    private BigDecimal balance;

    @JSONField(ordinal = 13)
    private boolean enable;
}
```

```java [Data.java]
public class MockData {
    public static MockUser user() {
      MockUser user = new MockUser()
          .setName("张三")
          .setBirthDay(DateTime.now())
          .setRegisterDay(DateTime.now())
          .setIdCard("51343620000320711X")
          .setPhone("09127518479")
          .setTel("13800138000")
          .setAddress("xx市xx区xxxx街道xxx号")
          .setEmail("123456789@qq.com")
          .setPassword(RandomUtil.randomString(8))
          .setCarnumber("京A88888")
          .setStatus(StatusEnum.A)
          .setBalance(new BigDecimal(100.123));
        return user;
    }
```

```java [Controller.java]
@GetMapping("object")
public MockUser object() {
    return MockData.user();
}
```

:::

### 明文

```properties
fastboot.fastjson.desensitize=false # 默认
```

```json
{
  "code": 0,
  "data": {
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
  },
  "msg": "success",
  "status": true
}
```

### 脱敏

```properties
fastboot.fastjson.desensitize=true # 启用脱敏
```

```json
{
  "code": 0,
  "msg": "操作成功",
  "data": {
    "name": "张*",
    "birthDay": "2021-06-27 00:05:50",
    "registerDay": "2021-06-27",
    "idCard": "5***************1X",
    "phone": "0912*****79",
    "tel": "138****8000",
    "address": "xx市xx区xx********",
    "email": "1********@qq.com",
    "password": "********",
    "carnumber": "京A8***8",
    "status": "A",
    "balance": "100.12",
    "enable": false
  },
  "status": true
}
```
