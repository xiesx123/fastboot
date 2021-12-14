package com.xiesx.fastboot.support.validate.annotation.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.alibaba.fastjson.JSONValidator;
import com.xiesx.fastboot.support.validate.annotation.VJson;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

/**
 * @title VJsonRule.java
 * @description 验证JSON规则
 * @author xiesx
 * @date 2020-7-21 22:44:51
 */
public class VJsonRule implements ConstraintValidator<VJson, String> {

    @Override
    public void initialize(VJson json) {}

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StrUtil.isBlank(s)) {
            return false;
        }
        return JSONValidator.from(s).validate() && JSONUtil.isJson(s);
    }
}
