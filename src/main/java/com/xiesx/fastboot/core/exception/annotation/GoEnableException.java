package com.xiesx.fastboot.core.exception.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import com.xiesx.fastboot.core.exception.GlobalExceptionAdvice;
import com.xiesx.fastboot.support.validate.configuration.ValidatorCfg;

/**
 * @title GoEnableException.java
 * @description
 * @author xiesx
 * @date 2021-04-04 17:54:32
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({GlobalExceptionAdvice.class, ValidatorCfg.class})
@Documented
public @interface GoEnableException {
}
