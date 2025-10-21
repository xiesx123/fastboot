package com.xiesx.fastboot.support.actuator;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.base.result.R;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import lombok.extern.log4j.Log4j2;

@Data
@Builder
@FieldNameConstants(innerTypeName = "FIELDS")
@Log4j2
public class ActuatorContext {

  @Builder.Default String trace = IdUtil.fastSimpleUUID();

  @Builder.Default String title = StrUtil.EMPTY;

  @Builder.Default Envar env = Envar.builder().build();

  @Builder.Default LinkedHashMap<String, Object> context = Maps.newLinkedHashMap();

  @Builder.Default Object result = StrUtil.EMPTY_JSON;

  @Builder.Default String error = StrUtil.EMPTY;

  /** 环境 */
  public void env(Envar env) {
    this.env = env;
  }

  /** 上下文 */
  public void put(String key, Object val) {
    context.put(key, val);
  }

  public void put(Dict data) {
    put(data, true);
  }

  public void put(Dict data, boolean print) {
    if (print) {
      log.debug("{} 新增结果 {}", trace, R.toJsonStr(data));
    }
    context.putAll(data);
  }

  public Dict add(Dict data) {
    Dict dict = Dict.create();
    data.forEach(
        (k, v) -> {
          // 老值
          BigDecimal before = MapUtil.get(context, k, BigDecimal.class);
          // 新值
          BigDecimal after = NumberUtil.add(before, BigDecimal.valueOf(Convert.toLong(v)));
          // 更新值
          context.put(k, after);
          // 当前k的新v
          dict.set(k, after);
        });
    log.debug("{} {} -> 累积 {}", trace, R.toJsonStr(data), R.toJsonStr(dict));
    return dict;
  }

  public void clear() {
    context.clear();
  }

  public boolean exists(String key) {
    return context.containsKey(key);
  }

  public boolean isEmpty() {
    return context.isEmpty();
  }

  public Dict get() {
    // log.info( "所有结果 {}", IR.toJsonStr(context));
    Dict dict = Dict.create();
    dict.putAll(context);
    MapUtil.sort(dict);
    return dict;
  }

  public Object get(String key) {
    return context.get(key);
  }

  /** 结果 */
  public void result(Object result) {
    this.result = result;
  }

  @Data
  @Builder
  @FieldNameConstants(innerTypeName = "FIELDS")
  public static class Envar {

    /** 自定义参数 */
    @Builder.Default public Map<String, Object> custom = Maps.newConcurrentMap();

    /** 结果保存 */
    @Builder.Default public String saveDir = FileUtil.getTmpDirPath();

    /** 调试参数 */
    @Builder.Default public boolean debug = false;
  }

  public static void main(String[] args) {
    Dict dict =
        Dict.create()
            .set(
                "jump",
                Dict.create().set("b", 1).set("a", 1).set("2", 16).set("-1", 8).set("6", 11));
    Dict jump = MapUtil.get(dict, "jump", Dict.class);
    ActuatorContext context = ActuatorContext.builder().build();
    Console.log(MapUtil.sort(context.get()));
    Console.log(MapUtil.sort(context.add(jump)));
    Console.log(MapUtil.sort(context.add(Dict.create().set("2", 16))));
    Console.log(MapUtil.sort(context.get()));
  }
}
