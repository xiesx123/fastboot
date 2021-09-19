package com.xiesx.fastboot.support.validate.annotation.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.xiesx.fastboot.support.validate.annotation.VEmpty;

import cn.hutool.core.util.StrUtil;

/**
 * @title VEmptyRule.java
 * @description 验证数字规则
 * @author xiesx
 * @date 2020-7-21 22:45:00
 */
public class VEmptyRule implements ConstraintValidator<VEmpty, String> {

    @Override
    public void initialize(VEmpty number) {}

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return StrUtil.isNotBlank(s);
    }
}
