package com.xiesx.fastboot.support.actuator.callable;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.support.actuator.ActuatorContext;
import com.xiesx.fastboot.support.actuator.aviator.AviatorManager;
import com.xiesx.fastboot.support.actuator.node.HttpNode;
import com.xiesx.fastboot.support.request.HttpRequests;
import java.util.concurrent.Callable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Data
@EqualsAndHashCode(callSuper = false)
@Log4j2
public class HttpCallable implements Callable<Dict> {

  @NonNull HttpNode node;

  @NonNull ActuatorContext context;

  @Override
  public Dict call() throws Exception {
    // 任务跟踪
    String trace = context.getTrace();
    // 任务结果
    Dict result = Dict.create();
    // 验证规则
    boolean isInvoke = true;
    if (node.isAviator() && !context.isEmpty()) {
      isInvoke = AviatorManager.isInvoke(trace, node.rule(), context.getContext());
    }
    // 计时器
    TimeInterval timer = DateUtil.timer();
    log.debug("{} ===============执行请求开始==============>", context.getTrace());
    if (isInvoke) {
      log.debug("{} 请求地址 {}", trace, node.getUrl());
      log.debug("{} 请求参数 {}", trace, R.toJsonStr(node.params()));
      // 构造请求
      HttpRequest req =
          HttpRequest.of(node.getUrl()).method(node.getMethod()).timeout(node.getTimeout());
      // 请求重试
      HttpResponse res = HttpRequests.retry(req);
      // 获取结果
      String body = res.body();
      log.debug("{} 请求响应 {}", trace, R.toJsonStr(body));
      // 判断格式
      if (!JSONUtil.isTypeJSON(body)) {
        result.set(ActuatorContext.FIELDS.error, R.fail("响应数据格式错误"));
      }
      // 判断状态，记录所有结果，传递下个任务使用
      JSONObject json = JSON.parseObject(body);
      if (res.isOk()) {
        result.set(node.ret(), json);
      } else
      // （错误 + 不忽略），记录error信息
      if (!res.isOk() && !node.ignoreFailure()) {
        result.set(ActuatorContext.FIELDS.error, R.fail(body));
      } else {
        throw new RuntimeException(StrUtil.format("{} 调度执行 {} 已终止 {}", trace, R.toJsonStr(json)));
      }
    } else {
      log.debug("请求忽略 【{}】", node.name());
      // 从上一次的结果中取值
      result.putIfAbsent(node.ret(), "ignore");
    }
    log.debug("{} 执行结果 {}", trace, R.toJsonStr(result.getObj(node.ret())));
    log.debug(
        "{} ===============执行请求结束==============> 耗时{}ms\r\n",
        context.getTrace(),
        timer.intervalMs());
    return result;
  }
}
