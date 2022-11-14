package com.xiesx.fastboot.support.actuator.aviator.funtion;

import java.util.Map;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import lombok.extern.log4j.Log4j2;

/**
 * @title CompareFunction.java
 * @description
 * @author xiesx
 * @date 2021-11-12 17:45:40
 */
@Log4j2
public class CompareFunction extends AbstractFunction {

    private static final long serialVersionUID = 1L;

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        Number left = FunctionUtils.getNumberValue(arg1, env);
        Number right = FunctionUtils.getNumberValue(arg2, env);
        AviatorBoolean bool = right.intValue() > left.intValue() ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
        log.debug("{}({},{}) -> {}", getName(), left, right, FunctionUtils.getBooleanValue(bool, env));
        return bool;
    }

    @Override
    public String getName() {
        return "compare";
    }
}
