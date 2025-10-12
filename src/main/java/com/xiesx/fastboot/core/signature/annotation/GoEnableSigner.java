package com.xiesx.fastboot.core.signature.annotation;

import com.xiesx.fastboot.core.signature.configuration.SignerCfg;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({SignerCfg.class})
@Documented
public @interface GoEnableSigner {}
