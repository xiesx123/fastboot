package com.xiesx.fastboot.support.actuator.aviator.funtion;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SumFunction extends AbstractFunction {

  private static final long serialVersionUID = 1L;

  @Override
  public AviatorObject call(Map<String, Object> env, AviatorObject args1, AviatorObject args2) {
    Number left = FunctionUtils.getNumberValue(args1, env);
    Number right = FunctionUtils.getNumberValue(args2, env);
    AviatorDouble doub = new AviatorDouble(left.doubleValue() + right.doubleValue());
    log.debug("{}({},{}) -> {}", getName(), left, right, FunctionUtils.getNumberValue(doub, env));
    return doub;
  }

  @Override
  public String getName() {
    return "sum";
  }
}
