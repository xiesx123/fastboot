package com.xiesx.fastboot.support.validate.annotation.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.xiesx.fastboot.support.validate.annotation.VNumber;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;

/**
 * @title VNumberRule.java
 * @description 验证数字规则
 * @author xiesx
 * @date 2020-7-21 22:45:00
 */
public class VNumberRule implements ConstraintValidator<VNumber, String> {

    @Override
    public void initialize(VNumber number) {}

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Validator.isNumber(s);
    }

    public static void main(String[] args) {
        System.out.println(NumberUtil.isNumber("1385132691011141632"));
    }
}
