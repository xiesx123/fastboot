package com.xiesx.fastboot.support.validate.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import cn.hutool.core.lang.RegexPool;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Documented
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
// 不能为空
@NotBlank(message = "{fastboot.empty}")
// 手机号
@Pattern(regexp = RegexPool.MOBILE, message = "{fastboot.mobile}")
public @interface VMobile {

  String message() default "{fastboot.error}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
