package com.xiesx.fastboot.core.advice.annotation;

import com.xiesx.fastboot.core.advice.GlobalBodyAdvice;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({GlobalBodyAdvice.class})
@Documented
public @interface GoEnableBody {}
