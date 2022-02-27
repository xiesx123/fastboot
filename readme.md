# FastBoot

> 基于 [`spring-boot v2.6.4`](https://spring.io/projects/spring-boot) 开发，结合工作中封装拓展而来，按需开启，快速开发

### 安装

[![](https://www.jitpack.io/v/com.gitee.xiesixian/fast-boot.svg)](https://www.jitpack.io/#com.gitee.xiesixian/fast-boot)

### 启动

```
    ______           __  ____              __ 
   / ____/___ ______/ /_/ __ )____  ____  / /_
  / /_  / __ `/ ___/ __/ __  / __ \/ __ \/ __/
 / __/ / /_/ (__  ) /_/ /_/ / /_/ / /_/ / /_  
/_/    \__,_/____/\__/_____/\____/\____/\__/  Power By SpringBoot (v2.6.4) 


[FastBoot][ INFO][06-17 15:52:13]-->[restartedMain:914820][logStarting(StartupInfoLogger.java:55)] | - Starting FastBootApplication using Java 1.8.0_282 on DESKTOP-OEU754I with PID 96 (D:\Projects\gotv\fast-boot\target\test-classes started by Administrator in D:\Projects\gotv\fast-boot)
[FastBoot][ INFO][06-17 15:52:13]-->[restartedMain:914822][logStartupProfileInfo(SpringApplication.java:679)] | - The following profiles are active: mysql
[FastBoot][ INFO][06-17 15:52:14]-->[restartedMain:915877][<init>(HikariDataSource.java:80)] | - master - Starting...
[FastBoot][ INFO][06-17 15:52:14]-->[restartedMain:915884][<init>(HikariDataSource.java:82)] | - master - Start completed.
[FastBoot][ INFO][06-17 15:52:14]-->[restartedMain:915884][addDataSource(DynamicRoutingDataSource.java:144)] | - dynamic-datasource - add a datasource named [master] success
[FastBoot][ INFO][06-17 15:52:14]-->[restartedMain:915886][afterPropertiesSet(DynamicRoutingDataSource.java:235)] | - dynamic-datasource initial loaded [1] datasource,primary datasource named [master]
[FastBoot][ INFO][06-17 15:52:15]-->[restartedMain:916916][init(SpringStartup.java:60)] | - Startup Server name: fast-boot, path: d:/projects/gotv/fast-boot
[FastBoot][ INFO][06-17 15:52:15]-->[restartedMain:916965][scheduler(SpringStartup.java:83)] | - Startup Scheduler 0 Job Completed.
[FastBoot][ INFO][06-17 15:52:15]-->[restartedMain:916966][setApplicationContext(SpringContext.java:27)] | - Spring ApplicationContext completed.
[FastBoot][ INFO][06-17 15:52:15]-->[restartedMain:916967][<init>(SpringStartup.java:27)] | - SpringStartup constructor
[FastBoot][ INFO][06-17 15:52:15]-->[restartedMain:916970][postConstruct(SpringStartup.java:65)] | - SpringStartup postConstruct
[FastBoot][ INFO][06-17 15:52:15]-->[restartedMain:916970][afterPropertiesSet(SpringStartup.java:74)] | - SpringStartup afterPropertiesSet
[FastBoot][ INFO][06-17 15:52:15]-->[restartedMain:916987][register(EventBusHelper.java:76)] | - Registered event : class com.xiesx.fastboot.core.eventbus.SimpleEventHandler
[FastBoot][ INFO][06-17 15:52:16]-->[restartedMain:917663][logStarted(StartupInfoLogger.java:61)] | - Started FastBootApplication in 2.922 seconds (JVM running for 917.663)
[FastBoot][ INFO][06-17 15:52:16]-->[restartedMain:917666][main(FastBootApplication.java:28)] | - Started FastBootApplication 启动成功
```

### 注解

|注解|功能|
|--|--|
|@GoEnableBody|启用统一返回|
|@GoEnableEventBus|启用事件总线|
|@GoEnableException|启用全局异常|
|@GoEnableFastJson|启用数据转换|
|@GoEnableLimiter|启用请求限流|
|@GoEnableLogger|启用日志打印|
|@@GoEnableSigner|启用数据签名|
|@GoEnableToken|启用令牌认证|

上述注解按需开启即可使用，如下：

```
@Configuration
// 启用统一返回
@GoEnableBody
// 启用事件总线
@GoEnableEventBus
// 启用全局异常
@GoEnableException
// 启用FastJson
@GoEnableFastJson
// 启用请求限流
@GoEnableLimiter
// 启用日志打印
@GoEnableLogger
// 启用数据签名
@GoEnableSigner
// 启用令牌认证
@GoEnableToken
public class GoCfg {
}
```

### 配置

yml配置如下

```
fastboot:                                                   # fastboot
  # ======= 数据转换 =======
  fastjson:
    supported-media-types:
    - application/json
    config:
      desensitize: true
      charset: utf-8
      date-format: yyyy-MM-dd HH:mm:ss
      serializer-features:
        - PrettyFormat                    # 格式化输出
        - WriteNullBooleanAsFalse         # 布尔类型如果为null，输出为false，而不是null
        - WriteEnumUsingToString          # 枚举类型用ToString输出
        ...

  # ======= 数据签名 =======
  signer:
    header: signer
    secret: fastboot!@#
    
  # ======= 令牌认证 =======
  token:
    header: token
    include-paths:
    - /api/**
    exclude-paths:
    - /js/**
    
  # ======= 证书配置 =======
  license:
    subject: fastboot
    public-alias: publicCert
    store-pass: 123456789@qq.com
    public-store-path: E:/license/publicCerts.store
    license-path: E:/license/license.lic
    
  # ======= 对象存储 =======
  minio:
    enabled: false
    address: 192.168.200.168:9090
    bucket: fastboot
    accessKey: minioadmin
    secretKey: minioadmin
```

> [`更多文档`](http://go168.xyz/)
