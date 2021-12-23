package com.xiesx.fastboot.core.fastjson.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import com.xiesx.fastboot.core.fastjson.configuration.FastJsonCfg;

/**
 * @title GoEnableFastJson.java
 * @description
 * @author xiesx
 * @date 2021-04-04 17:59:25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({FastJsonCfg.class})
@Documented
public @interface GoEnableFastJson {
}
