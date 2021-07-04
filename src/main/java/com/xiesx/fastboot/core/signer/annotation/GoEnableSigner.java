package com.xiesx.fastboot.core.signer.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import com.xiesx.fastboot.core.signer.cfg.SignerCfg;

/**
 * @title GoEnableSigner.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:04:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({SignerCfg.class})
@Documented
public @interface GoEnableSigner {
}
