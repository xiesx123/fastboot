package com.xiesx.fastboot.core.token.annotation;

import com.xiesx.fastboot.core.token.configuration.TokenCfg;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({TokenCfg.class})
@Documented
public @interface GoEnableToken {}
