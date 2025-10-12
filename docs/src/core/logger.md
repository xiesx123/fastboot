# 日志打印

xxx

## 注解

- `@GoEnableLogger`
- `@GoLogger`

## 参数

:::tip `@GoLogger`

- print：是否打印，默认开启 true
- format：是否格式化，默认关闭 false
- operation：方法说明，默认空""
- storage：日志存储，默认 LogStorageProvider（可实现 LogStorage 接口自定义保存）
  :::

## 示例

### 默认

默认打印、不格式化

```java
@GoLogger
@RequestMapping("print")
public Result print(BaseVo base, PageVo page) {
    return R.succ();
}
```

```log
2025-10-11 13:26:58 INFO http-nio-8080-exec-1:241517 LoggerAspect.java:105 - request print | [{"id":-1,"keyword":""},{"page":0,"limit":25,"size":25}]
2025-10-11 13:26:58 INFO http-nio-8080-exec-1:241517 LoggerAspect.java:118 - response took 0 ms | print | {"code":0,"msg":"success"}
```

### 屏蔽

单独某个方法不输出日志

```java
@GoLogger(print = false)
@RequestMapping("noprint")
public Result noprint(BaseVo base, PageVo page) {
    return R.succ();
}
```

```log
无输出
```

### 格式化

格式化输入、输出参数

```java
@GoLogger(format = true)
@RequestMapping("format")
public Result format(BaseVo base, PageVo page) {
    return R.succ();
}
```

```log
2025-10-11 13:29:15 INFO http-nio-8080-exec-8:378681 LoggerAspect.java:105 - request format | [
    {
        "id": -1,
        "keyword": ""
    },
    {
        "page": 0,
        "limit": 25,
        "size": 25
    }
]
2025-10-11 13:29:15 INFO http-nio-8080-exec-8:378682 LoggerAspect.java:118 - response took 1 ms | format | {
    "code": 0,
    "msg": "success"
}
```

### 优先级

类注解优先级大于方法注解

```java
@RestController
@RequestMapping("body")
@GoLogger(storage = LogStorageMysqlProvider.class)
public class BodyController {

    @GetMapping("result")
    public Result result() {
        return R.succ(MockData.map());
    }
 }
```

```log
2025-10-11 13:29:42 INFO http-nio-8080-exec-9:406100 LoggerAspect.java:105 - request result | []
2025-10-11 13:29:42 INFO http-nio-8080-exec-9:406102 LoggerAspect.java:118 - response took 1 ms | result | {"code":0,"msg":"success","data":{"k1":"1","k2":2,"k3":3,"k4":4.1,"k5":5.2,"k6":true,"k7":"7","k8":1760160582815}}
2025-10-11 13:29:42 INFO http-nio-8080-exec-9:406104 Slf4JLogger.java:60 - took 0 ms | statement | insert into xx_log (create_date,del,ip,method,req,res,time,type,update_date,url,id) values ('2025-10-11T13:29:42.817+0800',0,'127.0.0.1','result','{}','{"code":0,"msg":"success","data":{"k1":"1","k2":2,"k3":3,"k4":4.1,"k5":5.2,"k6":true,"k7":"7","k8":1760160582815}}',1,'GET','2025-10-11T13:29:42.817+0800','/body/result',1976882883608051712)
2025-10-11 13:29:42 INFO http-nio-8080-exec-9:406109 Slf4JLogger.java:60 - took 3 ms | commit |
2025-10-11 13:29:42 INFO http-nio-8080-exec-9:406109 LogStorageSimpleProvider.java:37 - log id 1976882883608051712
```

## 存储

### 自定义

::: code-group

```sql [Schema.sql]
CREATE TABLE `xx_log` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `ip` varchar(255) DEFAULT NULL COMMENT '请求ip',
  `url` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `method` varchar(255) DEFAULT NULL COMMENT '请求方法',
  `type` varchar(255) DEFAULT NULL COMMENT '请求类型',
  `req` longtext DEFAULT NULL COMMENT '请求参数',
  `res` longtext DEFAULT NULL COMMENT '响应结果',
  `time` bigint(20) DEFAULT NULL COMMENT '请求耗时',
  `del` bit(1) DEFAULT NULL COMMENT '是否删除（0默认、1删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
```

```java [Entity.java]
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@FieldNameConstants(innerTypeName = "FIELDS")
@Table(name = "xx_log")
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@SQLRestriction("del=0")
@SQLDelete(sql = "update xx_log set del=1 where id = ?")
public class LogRecord extends JpaPlusEntity<LogRecord> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键，例（1408447004119666688）
	 */
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GeneratedIdWorker(centerId = 1, workerId = 0)
	@JSONField(ordinal = 1)
	private Long id;

	/**
	 * 创建时间
	 */
	@CreatedDate
	@Column(updatable = false, nullable = false)
	@JSONField(ordinal = 2)
	private Date createDate;

	/**
	 * 修改时间
	 */
	@LastModifiedDate
	@Column(nullable = false)
	@JSONField(ordinal = 3)
	private Date updateDate;

	/**
	 * 请求IP
	 */
	@Column
	@JSONField(ordinal = 4)
	private String ip;

	/**
	 * 方法
	 */
	@Column
	@JSONField(ordinal = 5)
	private String method;

	/**
	 * 方式
	 */
	@Column
	@JSONField(ordinal = 6)
	private String type;

	/**
	 * 地址
	 */
	@Column
	@JSONField(ordinal = 7)
	private String url;

	/**
	 * 请求参数
	 */
	@JSONField(serialize = false)
	@Lob
	private String req;

	/**
	 * 响应结果
	 */
	@Lob
	@JSONField(serialize = false)
	private String res;

	/**
	 * 执行时间（毫秒）
	 */
	@Column
	@JSONField(ordinal = 10)
	private Long time;

	/**
	 * 是否删除
	 */
	@Column
	@JSONField(serialize = false)
	private boolean del = false;

	// ======================

	@JSONField(serialize = false, ordinal = 8)
	public Object getParams() {
		return JSON.parse(req);
	}

	@JSONField(serialize = false, ordinal = 9)
	public Object getResult() {
		return JSON.parse(res);
	}
}
```

```java [Repository.java]
public interface LogRecordRepository extends JpaPlusRepository<LogRecord, String> {

}
```

```java [CustomProvider.java]
@Log4j2
public class CustomProvider extends LogStorageProvider {

    LogRecordRepository mLogRecordRepository;

    public CustomProvider(String operation, String method, Object[] args, Long time) {
        super(operation, method, args, time);
    }

    @Override
    public void record(Object result) {
        super.record(result);
        LogRecord logRecord =
                new LogRecord()
                        .setIp(ip)
                        .setMethod(method)
                        .setType(type)
                        .setUrl(uri)
                        .setReq(R.toJsonStr(parameters))
                        .setRes(R.toJsonStr(result))
                        .setTime(time);
        log.info(
                "log id {}",
                SpringHelper.getBean(LogRecordRepository.class).insertOrUpdate(logRecord).getId());
    }
}
```

:::

### 使用

```java
@RestController
@RequestMapping("logger")
public class LoggerController {

    @GoLogger(format = true, storage = CustomProvider.class)
    @RequestMapping("storage")
    public Result storage(BaseVo base, PageVo page) {
        return R.succ();
    }
 }
```

```log
2025-10-11 13:33:18 INFO http-nio-8080-exec-3:621493 LoggerAspect.java:105 - request storage | [
    {
        "id": -1,
        "keyword": ""
    },
    {
        "page": 0,
        "limit": 25,
        "size": 25
    }
]
2025-10-11 13:33:18 INFO http-nio-8080-exec-3:621493 LoggerAspect.java:118 - response took 0 ms | storage | {
    "code": 0,
    "msg": "success"
}
2025-10-11 13:33:18 INFO http-nio-8080-exec-3:621496 Slf4JLogger.java:60 - took 0 ms | statement | insert into xx_log (create_date,del,ip,method,req,res,time,type,update_date,url,id) values ('2025-10-11T13:33:18.208+0800',0,'127.0.0.1','storage','{}','{"code":0,"msg":"success"}',0,'GET','2025-10-11T13:33:18.208+0800','/logger/storage',1976883787023384576)
2025-10-11 13:33:18 INFO http-nio-8080-exec-3:621499 Slf4JLogger.java:60 - took 2 ms | commit |
2025-10-11 13:33:18 INFO http-nio-8080-exec-3:621499 LogStorageSimpleProvider.java:32 - log id 1976883787023384576
```

## p6spy

### 配置文件

```properties [spy.properties]
module.log=com.p6spy.engine.logging.P6LogFactory,com.p6spy.engine.outage.P6OutageFactory
# 自定义日志打印
logMessageFormat=com.xiesx.springboot.core.logger.pkaq.P6SpyLogger
# 使用日志系统记录sql
appender=com.p6spy.engine.spy.appender.Slf4JLogger
# 配置记录Log例外
excludecategories=info,debug,result,batc,resultset
# 设置使用p6spy driver代理
deregisterdrivers=true
# 日期格式
dateformat=yyyy-MM-dd HH:mm:ss
# 实际驱动
driverlist=com.mysql.cj.jdbc.Driver
# 是否开启慢SQL记录
outagedetection=true
# 慢SQL记录标准 （秒）
outagedetectioninterval=2
```

### 使用

实际使用中建议本地开发环境`application-local`使用`p6spy`

- 开发

```yml
spring:
  datasource:
    driver-class-name: com.p6spy.engine.spy.P6SpyDriver
    url: jdbc:p6spy:mysql://127.0.0.1:3307/dbname?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai
    username: xxxx
    password: xxxx
```

```log
2025-10-11 13:37:21 INFO main:3925 Slf4JLogger.java:60 - took 1 ms | statement | SELECT id, TYPE FROM xx_log WHERE ip = '127.0.1.1' AND type = 'GET'
```

- 生产

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3307/dbname?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai
    username: xxxx
    password: xxxx
```

```log
2025-10-11 13:37:21 INFO main:3925 Slf4JLogger.java:60 - took 1 ms | statement | SELECT id, TYPE FROM xx_log WHERE ip = ? AND type = ?
```
