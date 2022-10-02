package com.xiesx.fastboot.core.advice.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import com.xiesx.fastboot.core.advice.GlobalBodyAdvice;

/**
 * @title GoEnableBody.java
 * @description
 * @author xiesx
 * @date 2021-04-04 17:52:41
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({GlobalBodyAdvice.class})
@Documented
public @interface GoEnableBody {
}
