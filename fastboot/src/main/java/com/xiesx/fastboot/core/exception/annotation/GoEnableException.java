package com.xiesx.fastboot.core.exception.annotation;

import com.xiesx.fastboot.core.exception.GlobalExceptionAdvice;
import com.xiesx.fastboot.support.validate.configuration.ValidatorCfg;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({GlobalExceptionAdvice.class, ValidatorCfg.class})
@Documented
public @interface GoEnableException {}
