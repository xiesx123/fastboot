package com.xiesx.fastboot.core.advice.annotation;

import com.xiesx.fastboot.core.advice.GlobalBodyAdvice;
import java.lang.annotation.*;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({GlobalBodyAdvice.class})
@Documented
public @interface GoEnableBody {}
