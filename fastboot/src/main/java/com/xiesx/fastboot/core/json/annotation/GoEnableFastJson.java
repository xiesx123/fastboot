package com.xiesx.fastboot.core.json.annotation;

import com.xiesx.fastboot.core.json.configuration.FastJsonCfg;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({FastJsonCfg.class})
@Documented
public @interface GoEnableFastJson {}
