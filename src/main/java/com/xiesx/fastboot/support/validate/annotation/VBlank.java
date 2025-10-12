package com.xiesx.fastboot.support.validate.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.xiesx.fastboot.support.validate.annotation.constraint.VBlankRule;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

// 文档生成标识
@Documented
// 申明注解的作用位置
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER})
// 运行时机
@Retention(RUNTIME)
// 定义对应的校验器,自定义注解必须指定
@Constraint(validatedBy = {VBlankRule.class})
public @interface VBlank {

    String message() default "{fastboot.empty}"; // 错误提示信息默认值，可以使用el表达式。

    Class<?>[] groups() default {}; // 约束注解在验证时所属的组别

    Class<? extends Payload>[] payload() default {}; // 约束注解的有效负载
}
