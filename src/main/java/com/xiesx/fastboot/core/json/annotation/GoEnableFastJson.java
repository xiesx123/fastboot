package com.xiesx.fastboot.core.json.annotation;

import com.xiesx.fastboot.core.json.configuration.FastJsonCfg;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({FastJsonCfg.class})
@Documented
public @interface GoEnableFastJson {}
