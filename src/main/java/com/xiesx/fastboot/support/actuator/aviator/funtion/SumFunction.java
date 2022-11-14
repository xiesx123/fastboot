package com.xiesx.fastboot.support.actuator.aviator.funtion;

import java.util.Map;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import lombok.extern.log4j.Log4j2;

/**
 * @title SumFunction.java
 * @description
 * @author xiesx
 * @date 2021-11-12 17:45:40
 */
@Log4j2
public class SumFunction extends AbstractFunction {

    private static final long serialVersionUID = 1L;

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject args1, AviatorObject args2) {
        Number left = FunctionUtils.getNumberValue(args1, env);
        Number right = FunctionUtils.getNumberValue(args2, env);
        AviatorDouble doub = new AviatorDouble(left.doubleValue() + right.doubleValue());
        log.debug("{}({},{}) -> {}", getName(), left, right, FunctionUtils.getBooleanValue(doub, env));
        return doub;
    }

    @Override
    public String getName() {
        return "sum";
    }
}
