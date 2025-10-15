# 数据效验

该功能由[`Hibernate-validator`](http://hibernate.org/validator/releases/6.0/)封装而来

> 在任何时候，当你要处理一个应用程序的业务逻辑，数据校验是你必须要考虑和面对的事情。应用程序必须通过某种手段来确保输入进来的数据从语义上来讲是正确的，通常我们会有一个验证数据的过程，待这些验证过程完毕，结果无误后，参数才会进入到正式的业务处理中。

> 我们在开发过程中，会有各种各样的入参，会经常需要写一些校验的代码，比如字段非空，字段长度限制，邮箱格式验证等等，写这些与业务逻辑关系不大的代码个人感觉不够优雅：
>
> - 验证代码重复繁琐
> - 方法内代码冗余

`Hibernate-validator`应运而生，与持久层框架`Hibernate`没有什么关系，是对`JSR 380（Bean Validation 2.0）`、`JSR 303（Bean Validation 1.0）`规范的实现；部分注解如下：

## 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

## 示例

### 简单校验

> Hibernate 校验，失败则抛出 `ConstraintViolationException` 异常
> TestController.java

```java
@RestController
@RequestMapping(value = "/api")
@Validated
public class TestController{

    @RequestMapping("test")
    public Result test(@NotNull(message = "参数不能为空") String key) {
        return R.succ(key);
    }
}
```

```log
[FastBoot][ERROR][08-29 17:19:55]-->[http-nio-9090-exec-3:39648][validatorException(GlobalExceptionAdvice.java:84)] | - validatorException ......
javax.validation.ConstraintViolationException: test.key: 参数不能为空
	at org.springframework.validation.beanvalidation.MethodValidationInterceptor.invoke(MethodValidationInterceptor.java:116) ~[spring-context-5.2.8.RELEASE.jar:5.2.8.RELEASE]
```

```json
{
  "code": 3000,
  "msg": "效验错误",
  "data": ["test.key 参数不能为空"],
  "success": false
}
```

> `Spring` 校验，失败则抛出 `BindException` 异常

```java
@Data
public class TestVo {
    @NotNull(message = "参数不能为空")
    private String key;
}

@RestController
@RequestMapping(value = "/api")
public class TestController{

    @RequestMapping("test")
    public Result test(@Validated TestVo vo) {
        return R.succ(vo.getKey());
    }
}
```

```log
[FastBoot][ERROR][08-29 17:28:30]-->[http-nio-9090-exec-1:24367][validatorException(GlobalExceptionAdvice.java:84)] | - validatorException ......
org.springframework.validation.BindException: org.springframework.validation.BeanPropertyBindingResult: 1 errors
Field error in object 'testVo' on field 'key': rejected value [null]; codes [NotNull.testVo.key,NotNull.key,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [testVo.key,key]; arguments []; default message [key]]; default message [参数不能为空]
	at org.springframework.web.method.annotation.ModelAttributeMethodProcessor.resolveArgument(ModelAttributeMethodProcessor.java:164) ~[spring-web-5.2.8.RELEASE.jar:5.2.8.RELEASE]
```

```json
{
  "code": 3000,
  "msg": "效验错误",
  "data": ["key 参数不能为空"],
  "success": false
}
```

不管是方式 1/2 都，校验失败后会被全局`异常捕获`处理，再`统一返回`规定格式

### 分组校验

实际开发场景中：

> 查询用户接口 A，必传：id

> 修改昵称接口 B，必传：id，nickname

> 修改手机接口 C，必传：id，code，phone

假设我们每类业务逻辑都有一个 VO 来接收参数，每个定义 N 个字段，但每次操作并不需要所有都是必传，可能只是个别参数必传，如何优雅的解决？
TestVo.java

```java
@Data
public class TestVo {

    @NotNull(message = "用户ID不能为空")
    private String id;

    @NotNull(message = "昵称不能为空", groups = B.class)
    private String nickname;

    @NotNull(message = "验证码不能为空", groups = C.class)
    private String code;

    @NotNull(message = "手机不能为空", groups = C.class)
    private String phone;

    public interface TestVoValid {
        public interface A{}
        public interface B{}
        public interface C{}
    }
}
```

TestController.java

```java
@RestController
@RequestMapping(value = "/api")
public class TestController{

    // 校验，默认分组 Default.class (注：没有使用分组A，默认使用Default)
    @RequestMapping("test")
    public Result test(@Validated TestVo vo) {
        return R.succ(vo.getId());
    }

    // 校验，默认分组 Default.class + B.class
    @RequestMapping("update")
    public Result update(@Validated(value = {Default.class, B.class}) TestVo vo) {
        return R.succ(vo.getNickname());
    }

    // 校验，默认分组 Default.class + C.class
    @RequestMapping("change")
    public Result change(@Validated(value = {Default.class, C.class}) TestVo vo) {
        return R.succ(vo.getPhone());
    }
}
```

校验结果

> 本框架默认是`快速模式`即只要有一个不通过就返回；
> 原默认是`普通模式`即会返回所有的验证不通过信息集合

1、当请求 A 时，id 不传时，校验错误
默认、快速模式均返回

```json
{
  "code": 3000,
  "msg": "校验错误",
  "data": ["id 用户ID不能为空"],
  "success": false
}
```

2、当请求 B 时，id、nickname 有一个不传时，校验错误
2.1 快速模式：

```json
{ "code": 3000, "msg": "校验错误", "data": ["xxx不能为空"], "success": false }
```

2.2 默认模式

```json
{
  "code": 3000,
  "msg": "校验错误",
  "data": ["id 用户ID不能为空", "nickname 昵称不能为空"],
  "success": false
}
```

3、当请求 C 时，id、code、phone 有一个不传时，校验错误
3.1 快速模式：

```json
{ "code": 3000, "msg": "校验错误", "data": ["xxx不能为空"], "success": false }
```

3.2 默认模式

```json
{
  "code": 3000,
  "msg": "校验错误",
  "data": ["id 用户ID不能为空", "code 验证码不能为空", "phone 手机不能为空"],
  "success": false
}
```

### 自定义效验

在上述分组示例中，很明显手机号是 11 位数字类型，在内置注解中无法只用单个注解同时满足 3 个条件

> - 长度 11 位
> - 0-9 数字组成
> - 手机号段

所以需要自定义处理，这里又可以分 2 种：

> - 组合验证：通过内置的注解组合验证（如：验证手机号）

VMobile.java

```java
@Documented
// 申明注解的作用位置
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER})
// 运行时机
@Retention(RUNTIME)
// 定义对应的校验器,自定义注解必须指定
@Constraint(validatedBy = {})
// 不能为空
@NotBlank(message = "{FastBoot.empty}")
// 验证手机号（第1为：1） + （第2位：可能是3/4/5/7/8等的任意一个） + （第3位：0-9） + d表示数字[0-9]的8位，总共加起来11位
@Pattern(regexp = "(?:0|86|\\+86)?1[3-9]\\d{9}", message = "{fb.mobile}")
public @interface VMobile {

    String message() default "{FastBoot.error}";// 错误提示信息默认值，可以使用el表达式。

    Class<?>[] groups() default {};// 约束注解在验证时所属的组别

    Class<? extends Payload>[] payload() default {};// 约束注解的有效负载
}
```

> - 自定义校验器：通过校验器来验证（如：验证 JSON 格式）

VJson.java

```java
@Documented
// 申明注解的作用位置
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER})
// 运行时机
@Retention(RUNTIME)
// 定义对应的校验器,自定义注解必须指定
@Constraint(validatedBy = {VJsonRule.class})
// 不能为空
@NotBlank(message = "{FastBoot.empty}")
public @interface VJson {

    String message() default "{FastBoot.json}";// 错误提示信息默认值，可以使用el表达式。

    Class<?>[] groups() default {};// 约束注解在验证时所属的组别

    Class<? extends Payload>[] payload() default {};// 约束注解的有效负载
}
```

VJsonRule.java

```java
public class VJsonRule implements ConstraintValidator<VJson, String> {

    @Override
    public void initialize(VJson json) {}

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        ·if (CharSequenceUtil.isBlank(s)) {
            return false;
        }
        return JSONValidator.from(s).validate();
    }
}
```

在 VO 中，则可以使用我们自定义的@VMobile、@VJson 注解，无需重复设置 message

```java
@VMobile
private String phone;

@VJson
private String json;
```

### 对象校验

上述 1.2.3 都是接收数据时校验 VO，那么在业务对象 BO，持久对象 PO 这类如何去校验？

```java
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FastBootApplication.class)
public class ValidatorHelperTest {

    @Autowired
    Validator validator;

    @Test
    public void verify1() {
        TestVo vo = new TestVo();
        Set<ConstraintViolation<TestVo>> violations = validator.validate(vo, Default.class);
        List<String> message = ValidatorHelper.extractMessage(violations);
        log.info(JSON.toJSONString(R.succ(message)));
    }

    @Test
    public void verify2() {
        TestVo vo = new TestVo();
        try {
            ValidatorHelper.validate(vo);
        } catch (ConstraintViolationException e) {
            // 打印 messgae
            List<String> message1 = ValidatorHelper.extractMessage(e);
            log.info(JSON.toJSONString(R.succ(message1)));

            // 打印property + messgae
            Map<String, String> message2 = ValidatorHelper.extractPropertyAndMessage(e);
            log.info(JSON.toJSONString(R.succ(message2)));

            // 打印property + messgae
            List<String> message3 = ValidatorHelper.extractPropertyAndMessageAsList(e);
            log.info(JSON.toJSONString(R.succ(message3)));
        }
    }
}
```

本框架中默认注入 Validator，直接使用即可
方式 1

```json
@Autowired
Validator validator;
```

方式 2

```json
Validator validator = SpringHelper.getBean(Validator.class);
```

封装方法

```json
public static void validate(Object object) throws ConstraintViolationException
public static void validate(Object object, Class<?>... groups) throws ConstraintViolationException

public static List<String> extractMessage(ConstraintViolationException e)
public static List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations)

public static Map<String, String> extractPropertyAndMessage(ConstraintViolationException e)
public static Map<String, String> extractPropertyAndMessage(Set<? extends ConstraintViolation> constraintViolations)

public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e)
public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations)
public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e, String separator)
public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations, String separator)

```

校验结果

```log
[FastBoot][ INFO][08-29 23:14:28]-->[main: 5648][verify1(ValidatorHelperTest.java:39)] | - {"code":0,"msg": "success","data":["参数不能为空"],"success":true}
[FastBoot][ INFO][08-29 23:14:28]-->[main: 5660][verify2(ValidatorHelperTest.java:50)] | - {"code":0,"msg": "success","data":["参数不能为空"],"success":true}
[FastBoot][ INFO][08-29 23:14:28]-->[main: 5662][verify2(ValidatorHelperTest.java:54)] | - {"code":0,"msg": "success","data":{"id":"参数不能为空"},"success":true}
[FastBoot][ INFO][08-29 23:14:28]-->[main: 5663][verify2(ValidatorHelperTest.java:58)] | - {"code":0,"msg": "success","data":["id 参数不能为空"],"success":true}
```

以上 4 点是在编写 GOTV 服务中最常用的效验方式。`Hibernate-Validator`远不仅如此，还有很多如校验方式... 当实际场景已无法满足或用起来不是那么优雅的时候，更多见[官方文档](http://hibernate.org/validator/documentation/)~
