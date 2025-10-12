package com.xiesx.fastboot.core.limiter.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GoLimiter {

    double limit() default Integer.MAX_VALUE;

    String message() default "";
}
