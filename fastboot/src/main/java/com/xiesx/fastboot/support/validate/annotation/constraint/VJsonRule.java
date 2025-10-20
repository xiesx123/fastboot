package com.xiesx.fastboot.support.validate.annotation.constraint;

import cn.hutool.json.JSONUtil;
import com.xiesx.fastboot.support.validate.annotation.VJson;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VJsonRule implements ConstraintValidator<VJson, String> {

  @Override
  public void initialize(VJson json) {}

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return JSONUtil.isTypeJSON(s);
  }
}
