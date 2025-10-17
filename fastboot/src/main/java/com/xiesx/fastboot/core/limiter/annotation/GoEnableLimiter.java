package com.xiesx.fastboot.core.limiter.annotation;

import com.xiesx.fastboot.core.limiter.configuration.LimiterCfg;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({LimiterCfg.class})
@Documented
public @interface GoEnableLimiter {}
