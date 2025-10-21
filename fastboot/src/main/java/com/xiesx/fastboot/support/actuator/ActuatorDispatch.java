package com.xiesx.fastboot.support.actuator;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.support.actuator.callable.EnvCallable;
import com.xiesx.fastboot.support.actuator.node.NodePerformFunction;
import com.xiesx.fastboot.support.async.Async;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class ActuatorDispatch {

  @NonNull ActuatorContext context;

  @NonNull List<Object> nodes;

  @NonNull IActuatorCallback<? super Dict> callback;

  /** 调度执行 */
  public void execute() {
    String trace = context.getTrace();
    String title = context.getTitle();
    // 当前环境
    log.debug(">>>调度开始 {} {}", title, trace);
    try {
      // 预处理任务
      ListenableFuture env = Async.submit(new EnvCallable(context.getEnv()));
      // 执行任务，任务结果向后传递，A=1,B=2+A,C=3+B（C=3+2+1）依次类推
      List<ListenableFuture<Dict>> list = Lists.newArrayList(env);
      for (int i = 0; i < nodes.size(); i++) {
        Object node = nodes.get(i);
        list.add(
            Futures.transform(
                list.get(i == 0 ? 0 : i),
                new NodePerformFunction(node, context),
                Async.getExecutorService()));
      }
      // 最后任务
      ListenableFuture<Dict> future = list.get(list.size() - 1);
      // 同步执行
      Dict dict = future.get();
      // 获取上下文中错误信息
      if (ObjectUtil.isEmpty(MapUtil.getAny(dict, ActuatorContext.FIELDS.error))) {
        context.put(dict);
        callback.onSuccess(context, dict);
      } else {
        Result r = MapUtil.get(dict, ActuatorContext.FIELDS.error, Result.class);
        callback.onFailure(context, ExceptionUtil.wrapRuntime(r.msg()));
      }
    } catch (Exception e) {
      callback.onFailure(context, ExceptionUtil.wrapRuntime(e));
    }
    log.debug(">>>调度结束 {}\r\n", trace);
  }
}
