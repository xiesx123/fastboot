package com.xiesx.fastboot.support.actuator.node;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.core.json.reference.GenericType;
import com.xiesx.fastboot.support.actuator.ActuatorContext;
import com.xiesx.fastboot.support.actuator.ActuatorContext.Envar;
import com.xiesx.fastboot.support.actuator.callable.HttpCallable;
import com.xiesx.fastboot.support.async.Async;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.Nullable;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Log4j2
public class NodePerformFunction implements Function<Dict, Dict> {

  GenericType<AbstractNode> gtNode = new GenericType<AbstractNode>() {};

  GenericType<HttpNode> gtHttpNode = new GenericType<HttpNode>() {};

  @NonNull Object node;

  @NonNull ActuatorContext context;

  @Override
  public @Nullable Dict apply(@Nullable Dict input) {
    // 任务跟踪
    String trace = context.getTrace();
    // 环境信息
    Envar env = context.getEnv();
    // 如果出错，立即停止
    if (StrUtil.isNotBlank(input.getStr(ActuatorContext.FIELDS.error))) {
      return input;
    }
    // 保存上次结果
    context.put(input, env.isDebug());
    // 执行任务集
    List<Callable<Dict>> callables = Lists.newArrayList();
    // 计算单个多个
    List<JSONObject> jos = Lists.newArrayList();
    String json = node.toString();
    if (node instanceof JSONObject) {
      jos.add(JSON.parseObject(json));
    } else if (node instanceof JSONArray) {
      jos.addAll(JSON.parseArray(json, JSONObject.class));
    }
    for (JSONObject jo : jos) {
      // 节点信息（父类）
      AbstractNode plan = gtNode.parseObject(jo.toJSONString());
      log.debug("{} 创建请求: 【{}】", trace, plan.name());
      // 转换参数
      Map<String, Object> convers = extract(plan.params(), context.get());
      // 比对前后是否一致
      if (!Objects.equal(plan.params(), convers)) {
        log.debug("{} 原始参数 {}", trace, R.toJsonStr(plan.params()));
        plan.params(convers); // 注意：更新结果
        jo.put(AbstractNode.FIELDS.params, plan.params());
        log.debug("{} 转换参数 {}", trace, R.toJsonStr(plan.params()));
      }
      // 判断类型
      if (plan.type().isHttp()) {
        callables.add(new HttpCallable(gtHttpNode.parseObject(jo.toJSONString()), context));
      }
    }
    log.debug("{} 等待执行数量 {} 个", trace, callables.size());
    // 记录本次结果
    Dict result = Dict.create();
    List<Future<Dict>> futures = Lists.newArrayList();
    try {
      futures = Async.invokeAll(callables);
      for (Future<Dict> future : futures) {
        if (future.isDone()) {
          try {
            Dict dict = future.get();
            result.putAll(dict);
          } catch (Exception e) {
            String msg = ExceptionUtil.getSimpleMessage(e);
            log.error("{} processor function apply error {}", trace, msg);
            result.set(ActuatorContext.FIELDS.error, R.error(msg));
          }
        }
      }
    } catch (InterruptedException e) {
      String msg = ExceptionUtil.getSimpleMessage(e);
      log.error("{} invoke all {}", trace, msg);
    }
    // 传递本次结果
    return result;
  }

  /** 参数提取 */
  public static Map<String, Object> extract(
      Map<String, Object> params, Map<String, Object> templates) {
    // 参数构造
    Map<String, Object> map = Maps.newConcurrentMap();
    // 判断模板
    if (templates.isEmpty()) {
      return map;
    }
    // 循环处理老的参数
    params.forEach(
        (k, v) -> {
          String val = v.toString();
          if (StrUtil.contains(val, '$')) {
            try {
              map.put(k, R.eval(templates, val));
            } catch (Exception e) {
              // log.error("extract", e);
              map.put(k, "");
            }
          } else {
            map.put(k, v);
          }
        });
    return map;
  }

  public static void main(String[] args) {
    Dict dict = Dict.create().set("x1", Dict.create().set("x2", Dict.create().set("x3", 1)));
    String json = R.toJsonPrettyStr(dict);
    Console.log(json);
    Console.log(StrUtil.format("{}", R.eval(json, "$.x1.x2.x3")));
    Console.log(StrUtil.format("{}", R.eval(dict, "$.x1.x2.x3")));
    // {x=$.y.test}
    Dict params = Dict.create().set("x", "$.y.test");
    Console.log(params);
    // {y={test=1}}
    Dict templates = Dict.create().set("y", Dict.create().set("test", 1));
    Console.log(templates);
    // 参数提取
    Console.log(extract(params, templates));
  }
}
