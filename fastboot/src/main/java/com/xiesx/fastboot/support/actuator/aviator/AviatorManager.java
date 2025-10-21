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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

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
      Map<String, Object> env = Maps.newConcurrentMap();
      Matcher matcher = Pattern.compile("\\$\\.([a-zA-Z0-9_\\.]+)").matcher(expression);
      StringBuffer sb = new StringBuffer();
      while (matcher.find()) {
        String path = matcher.group(1); // "a.b.c"
        Object value = R.eval(context, path); // 从 JSON 中解析出值
        String safeVar = path.replace('.', '_'); // 转换成合法变量名
        env.put(safeVar, value);
        matcher.appendReplacement(sb, safeVar);
      }
      matcher.appendTail(sb);
      String preparedExpr = sb.toString();
      log.debug("{} 预处理表达式: {} 变量: {}", trace, preparedExpr, R.toJsonStr(env));
      // 执行表达式
      Object result = AviatorEvaluator.execute(preparedExpr, env, true);
      return Convert.toBool(result);
    } catch (Exception e) {
      log.warn("{} 表达式执行异常: {} - {}", trace, expression, e.getMessage());
      return false;
    }
  }

  public static void main(String[] args) {
    Console.log(AviatorEvaluator.execute("sum(1,2)")); // 3

    Console.log(AviatorEvaluator.execute("compare(2,1)")); // true
    Console.log(AviatorEvaluator.execute("compare(1,2)")); // false
    Console.log(AviatorEvaluator.execute("compare(sum(1,2),2)")); // true

    Console.log(
        isInvoke(
            "invoke($.x1.x2,-4)",
            Dict.create()
                .set("x1", Dict.create().set("x2", Lists.newArrayList(-1, -2, -3))))); // true
    Console.log(
        isInvoke(
            "invoke($.x1.x2,0)",
            Dict.create()
                .set("x1", Dict.create().set("x2", Lists.newArrayList(1, 2, 3))))); // false

    Console.log(
        isInvoke(
            "compare(sum($.x.y,2),2)", Dict.create().set("x", Dict.create().set("y", 1)))); // true
  }
}
