package com.xiesx.fastboot.core.limiter.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import com.xiesx.fastboot.core.limiter.cfg.LimiterCfg;

/**
 * @title GoEnableLimiter.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:01:22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({LimiterCfg.class})
@Documented
public @interface GoEnableLimiter {
}
