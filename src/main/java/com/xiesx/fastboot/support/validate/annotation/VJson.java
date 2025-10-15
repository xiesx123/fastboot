package com.xiesx.fastboot.support.validate.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.xiesx.fastboot.support.validate.annotation.constraint.VJsonRule;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {VJsonRule.class})
// 不能为空
@NotBlank(message = "{fastboot.empty}")
public @interface VJson {

    String message() default "{fastboot.json}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
