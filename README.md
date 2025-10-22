<div align="center">

 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://xiesx123.github.io/fastboot/spring_light.png" />
   <source media="(prefers-color-scheme: light)" srcset="https://xiesx123.github.io/fastboot/spring_dark.png" />
   <img src="https://xiesx123.github.io/fastboot/spring_light.png" width="80" />
 </picture>

<h1 align="center">FastBoot</h1>

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/06e56b0ddea4428bbb72f9cbfa24e56f)](https://app.codacy.com/gh/xiesx123/fastboot?utm_source=github.com&utm_medium=referral&utm_content=xiesx123/fastboot&utm_campaign=Badge_Grade)
[![Jitpack](https://jitpack.io/v/xiesx123/fastboot.svg)](https://jitpack.io/#xiesx123/fastboot)
[![Coverage Status](https://coveralls.io/repos/github/xiesx123/fastboot/badge.svg?branch=master)](https://coveralls.io/github/xiesx123/fastboot?branch=master)
![Jitpack Month Downloads](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fjitpack.io%2Fapi%2Fdownloads%2Fcom.github.xiesx123%2Ffastboot&query=month&suffix=%20month&style=flat&label=Downloads&link=https%3A%2F%2Fjitpack.io%2F%23xiesx123%2Ffastboot&color=0078D7) 
![License](https://img.shields.io/badge/license-MIT-blue.svg?style=flat&label=License&color=0078D7)

[![VitePress](https://img.shields.io/badge/VitePress-Doc-3E63DD?logo=markdown)](https://xiesx123.github.io/fastboot)
[![OpenJDK](https://img.shields.io/badge/OpenJdk-8,21-red?logo=openjdk)](https://adoptium.net/zh-CN/temurin/releases)
[![Spring](https://img.shields.io/badge/Spring-2,3-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot#overview)


🚀 快速、高效、轻量级的 Spring Boot 开发，用于快速构建应用程序

</div>

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
