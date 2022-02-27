package com.xiesx.fastboot.support.validate.annotation.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.xiesx.fastboot.support.validate.annotation.VBlank;

import cn.hutool.core.util.StrUtil;

/**
 * @title VEmptyRule.java
 * @description
 * @author xiesx
 * @date 2022-02-27 21:11:52
 */
public class VBlankRule implements ConstraintValidator<VBlank, String> {

    @Override
    public void initialize(VBlank number) {}

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return StrUtil.isNotBlank(s);
    }
}
