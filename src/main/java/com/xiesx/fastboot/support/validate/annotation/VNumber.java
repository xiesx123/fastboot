package com.xiesx.fastboot.support.validate.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import cn.hutool.core.lang.RegexPool;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
// 不能为空
@NotBlank(message = "{fastboot.empty}")
// 数字类型
@Pattern(regexp = RegexPool.NUMBERS, message = "{fastboot.number}")
public @interface VNumber {

    String message() default "{fastboot.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
