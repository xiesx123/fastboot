package com.xiesx.fastboot.support.validate.annotation.constraint;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.alibaba.fastjson2.JSONValidator;
import com.xiesx.fastboot.support.validate.annotation.VJson;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VJsonRule implements ConstraintValidator<VJson, String> {

    @Override
    public void initialize(VJson json) {}

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StrUtil.isBlank(s)) {
            return false;
        }
        return JSONUtil.isTypeJSON(s) && JSONValidator.from(s).validate();
    }
}
