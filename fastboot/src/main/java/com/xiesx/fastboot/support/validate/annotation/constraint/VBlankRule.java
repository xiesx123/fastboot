package com.xiesx.fastboot.support.validate.annotation.constraint;

import cn.hutool.core.util.StrUtil;

import com.xiesx.fastboot.support.validate.annotation.VBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VBlankRule implements ConstraintValidator<VBlank, String> {

    @Override
    public void initialize(VBlank number) {}

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return StrUtil.isNotBlank(s);
    }
}
