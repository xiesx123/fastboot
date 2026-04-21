# FastBoot

🚀 快速、高效、轻量级的 Spring Boot 开发，用于快速构建应用程序

## 安装

- 添加仓库

```xml [pom.xml]
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```

- 添加依赖

```xml
<dependency>
    <groupId>com.github.xiesx123</groupId>
    <artifactId>fastboot</artifactId>
    <version>3.1.0</version>
    <classifier>jakarta</classifier> <!-- spring3 -->
</dependency>
```

## 注解

```java
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
public class FastBootCfg {}

// 启用持久化增强
@EnableJpaPlusRepositories
@SpringBootApplication
public class FastBootApplication {}
```

## 配置

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
      reader-features:                    # 反序列化
        - UseDoubleForDecimals
    desensitize: true                     # 启用脱敏
        ...

  signer:                                 # ======= 数据签名
    header: sign                          # 签名键，默认：sign
    secret: fastboot!@#                   # 加密串，默认：fastboot!@#

  token:                                  # ======= 令牌认证
    header: token                         # 令牌键，默认：token
    secret: fastboot!@#                   # 加密串，默认：fastboot!@#
    include-paths:                        # 包含路径
    - /api/**
    - /token/**
    exclude-paths:                        # 排除路径
    - /js/**
```

## 启动

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
