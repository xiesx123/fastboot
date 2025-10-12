# FastBoot

FastBoot 为创作者而生，专注于解决 视频翻译、跨语言配音、个性化配音 等内容创作流程中的高频重复工作。只需提供素材，将自动完成语音识别、翻译、合成、草稿导出，提升创作效率，释放创作潜力 ！

### 安装

```xml
<dependency>
  <groupId>com.xiesx.fastboot</groupId>
  <artifactId>fastboot</artifactId>
  <version>master-SNAPSHOT</version>
<dependency>
```

### 使用

```java
@Configuration
@GoEnableBody       // 启用统一返回
@GoEnableEventBus   // 启用事件总线
@GoEnableException  // 启用全局异常
@GoEnableFastJson   // 启用数据转换
@GoEnableLimiter    // 启用请求限流
@GoEnableLogger     // 启用日志打印
@GoEnableSigner     // 启用数据签名
@GoEnableToken      // 启用令牌认证
public class GoCfg {
}
```

### 配置

```yml
fastboot:                                 # fastboot
  advice:                                 # ======= 统一返回
    body-ignores-urls:                    # 忽略路径
    - /swagger-resources,/api-docs
    - /body/ignore

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
    desensitize: true                     # 启用脱敏
        ...

  signer:                                 # ======= 数据签名
    header: sign                          # 签名键，默认：sign
    secret: fastboot!@#                   # 加密串，默认：fastboot!@#

  token:                                  # ======= 令牌认证
    header: token                         # 令牌键，默认：token
    include-paths:                        # 包含路径
    - /api/**
    - /token/**
    exclude-paths:                        # 排除路径
    - /js/**
```

### 启动

```log
______        _  ______             _   
|  ___|      | | | ___ \           | |  
| |_ __ _ ___| |_| |_/ / ___   ___ | |_ 
|  _/ _` / __| __| ___ \/ _ \ / _ \| __|
| || (_| \__ \ |_| |_/ / (_) | (_) | |_ 
\_| \__,_|___/\__\____/ \___/ \___/ \__|   Power By SpringBoot (v3.4.0) 

2025-10-11 12:57:15 INFO restartedMain:699 StartupInfoLogger.java:53 - Starting FastBootApplication using Java 21.0.5 with PID 5152 (20222025-10-11 12:57:15 INFO restartedMain:703 SpringApplication.java:658 - The following 1 profile is active: "dev"
2025-10-11 12:57:18 INFO restartedMain:4439 SpringContext.java:60 - Startup Server name: fast-boot, path: D:\Projects\fast\fast-boot\    
2025-10-11 12:57:18 INFO restartedMain:4444 StartupInfoLogger.java:59 - Started FastBootApplication in 4.002 seconds (process running for 4.459)
2025-10-11 12:57:18 INFO restartedMain:4446 FastBootApplication.java:24 - Started FastBootApplication Successfully.
```