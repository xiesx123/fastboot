package com.xiesx.fastboot.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @title RunExc.java
 * @description 异常枚举处理类
 * @author xiesx
 * @date 2020-7-21 22:30:51
 */
@Getter
@AllArgsConstructor
public enum RunExc {

    RUNTIME(1000, "运行错误"), // --> GlobalExceptionAdvice --> runtimeException

    REQUEST(2000, "请求失败"), // --> GlobalExceptionAdvice --> requestException

    RETRY(2010, "重试失败"), // --> HttpRetryer

    LIMITER(2020, "请求限流"), // --> LimiterAspect
    
    VALIDATOR(3000, "校验错误"), // --> GlobalExceptionAdvice --> validatorException

    DBASE(4000, "数据错误"), // --> GlobalExceptionAdvice --> jdbcException

    TOKEN(5000, "令牌错误"), // --> TokenInterceptor

    SIGN(6000, "签名错误"), // --> SignerAspect

    MINIO(9000, "对象存储"), // --> MinioBucketClient | MinioObjectClient

    UNKNOWN(9999, "未知");

    private Integer code;

    private String msg;
}
