package com.xiesx.fastboot.core.body.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import com.xiesx.fastboot.core.logger.cfg.LoggerCfg;

/**
 * @title IgnoreBody.java
 * @description
 * @author xiesx
 * @date 2021-04-04 17:52:45
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Import({LoggerCfg.class})
@Documented
public @interface IgnoreBody {

    boolean print() default true;

    boolean format() default false;

    boolean storage() default false;
}
