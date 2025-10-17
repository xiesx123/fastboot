package com.xiesx.fastboot.support.validate.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.xiesx.fastboot.support.validate.annotation.constraint.VJsonRule;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;

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
