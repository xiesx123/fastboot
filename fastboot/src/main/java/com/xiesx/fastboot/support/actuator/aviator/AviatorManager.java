package com.xiesx.fastboot.support.actuator.aviator;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Options;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.support.actuator.aviator.funtion.CompareFunction;
import com.xiesx.fastboot.support.actuator.aviator.funtion.InvokeFunction;
import com.xiesx.fastboot.support.actuator.aviator.funtion.SumFunction;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Log4j2
@UtilityClass
public class AviatorManager {

    /** 注册处理器函数 */
    static {
        // 运行速度优先
        AviatorEvaluator.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.EVAL);
        // 注册处理器函数
        AviatorEvaluator.addFunction(new SumFunction()); // 累计求和
        AviatorEvaluator.addFunction(new CompareFunction()); // 布尔类型
        AviatorEvaluator.addFunction(new InvokeFunction()); // 是否执行
    }

    public boolean isInvoke(String expression, Map<String, Object> context) {
        return isInvoke("", expression, context);
    }

    public boolean isInvoke(String trace, String expression, Map<String, Object> context) {
        try {
            if (StrUtil.isBlank(expression)) {
                return false;
            }
            String path = StrUtil.subBetween(expression, "(", ",");
            Map<String, Object> env = Maps.newHashMap();
            env.put(path, R.eval(context, path));
            log.debug("{} 参数处理 {} {}", trace, expression, R.toJsonStr(env));
            return Convert.toBool(AviatorEvaluator.execute(expression, env, true));
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        Console.log(AviatorEvaluator.execute("sum(1,2)")); // 3
        Console.log(AviatorEvaluator.execute("compare(2,1)")); // false
        Console.log(AviatorEvaluator.execute("compare(1,2)")); // true

        Console.log(
                isInvoke(
                        "invoke($.x1.x2,-4)",
                        Dict.create()
                                .set(
                                        "x1",
                                        Dict.create()
                                                .set(
                                                        "x2",
                                                        Lists.newArrayList(-1, -2, -3))))); // true
        Console.log(
                isInvoke(
                        "invoke($.x1.x2,0)",
                        Dict.create()
                                .set(
                                        "x1",
                                        Dict.create()
                                                .set("x2", Lists.newArrayList(1, 2, 3))))); // false
    }
}
