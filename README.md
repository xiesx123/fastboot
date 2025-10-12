<div align="center">

<img src="https://xiesx123.github.io/fastboot/favicon.ico" width="80" />

<h1 align="center">FastBoot </h1>

快速、高效、轻量级的 Spring Boot 开发，用于快速构建应用程序

</div>

## 安装

[![](https://jitpack.io/v/xiesx123/fastboot.svg)](https://jitpack.io/#xiesx123/fastboot)

- 添加仓库 `JitPack`

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
    <artifactId>fastBoot</artifactId>
    <version>master-SNAPSHOT</version>
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
public class GoCfg {
}
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
