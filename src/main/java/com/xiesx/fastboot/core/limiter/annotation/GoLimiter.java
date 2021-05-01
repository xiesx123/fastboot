package com.xiesx.fastboot.core.limiter.annotation;

import java.lang.annotation.*;

/**
 * @title GoLimiter.java
 * @description 限流器
 * @author xiesx
 * @date 2020-7-21 22:34:43
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GoLimiter {

    String message() default "";

    double limit() default Double.MAX_VALUE;
}
