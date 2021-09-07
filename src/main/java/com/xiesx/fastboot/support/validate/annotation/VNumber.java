package com.xiesx.fastboot.support.validate.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

import com.xiesx.fastboot.support.validate.annotation.constraint.VNumberRule;

/**
 * @title VNumber.java
 * @description 数字判断
 * @author xiesx
 * @date 2020-7-21 22:44:29
 */
// 申明注解的作用位置
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER})
// 运行时机
@Retention(RUNTIME)
// 定义对应的校验器,自定义注解必须指定
@Constraint(validatedBy = {VNumberRule.class})
// 附带不能为空 TODO 效验错误
@NotNull(message = "{fastboot.empty}")
@Documented
public @interface VNumber {

    String message() default "{fastboot.number}";// 错误提示信息默认值，可以使用el表达式。

    Class<?>[] groups() default {};// 约束注解在验证时所属的组别

    Class<? extends Payload>[] payload() default {};// 约束注解的有效负载
}
