package com.xiesx.fastboot.support.actuator.aviator.funtion;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import cn.hutool.core.convert.Convert;
import lombok.extern.log4j.Log4j2;

/**
 * @title InvokeFunction.java
 * @description 数组某个值小于指定值时，返回执行true
 * @author xiesx
 * @date 2021-11-12 17:45:40
 */
@Log4j2
public class InvokeFunction extends AbstractFunction {

    private static final long serialVersionUID = 1L;

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        List<Double> left = Convert.toList(Double.class, arg1.getValue(env));
        Collections.sort(left);
        Number right = FunctionUtils.getNumberValue(arg2, env);
        AviatorBoolean bool = right.doubleValue() > left.get(0) ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
        log.debug("{}({},{}) -> {}", getName(), left, right, FunctionUtils.getBooleanValue(bool, env));
        return bool;
    }

    @Override
    public String getName() {
        return "invoke";
    }
}
