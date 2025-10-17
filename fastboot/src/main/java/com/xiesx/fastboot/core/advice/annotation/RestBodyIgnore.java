package com.xiesx.fastboot.core.advice.annotation;

import com.xiesx.fastboot.core.logger.configuration.LoggerCfg;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import({LoggerCfg.class})
@Documented
public @interface RestBodyIgnore {}
