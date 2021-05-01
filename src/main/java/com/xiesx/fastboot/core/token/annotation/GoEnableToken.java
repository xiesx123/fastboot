package com.xiesx.fastboot.core.token.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import com.xiesx.fastboot.core.token.cfg.TokenCfg;

/**
 * @title GoEnableToken.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:10:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({TokenCfg.class})
@Documented
public @interface GoEnableToken {
}
