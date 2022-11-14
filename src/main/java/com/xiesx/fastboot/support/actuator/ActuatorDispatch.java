package com.xiesx.fastboot.support.actuator;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.support.actuator.callable.EnvCallable;
import com.xiesx.fastboot.support.actuator.callback.ActuatorFutureCallback;
import com.xiesx.fastboot.support.actuator.callback.IActuatorCallback;
import com.xiesx.fastboot.support.actuator.funtion.PlanPerformFunction;
import com.xiesx.fastboot.support.actuator.model.ActuatorEnv;
import com.xiesx.fastboot.support.actuator.model.ActuatorPlan;
import com.xiesx.fastboot.support.async.Async;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @title ProcessorDispatch.java
 * @description 任务调度
 * @author xiesx
 * @date 2021-07-30 16:49:28
 */
@Log4j2
@AllArgsConstructor
@RequiredArgsConstructor
public class ActuatorDispatch {

    @NonNull
    ActuatorEnv env;

    @NonNull
    ActuatorPlan model;

    IActuatorCallback<? super Dict> callback;

    /**
     * 调度执行
     */
    public void execute() {
        // 当前环境
        ActuatorContext context = ActuatorContext.builder().trace(env.getTrace()).build();
        // 默认回调
        if (callback == null) {
            callback = new ActuatorFutureCallback(env);
        }
        log.debug(">>>调度开始 {} {}", model.getTitle(), env.getTrace());
        try {
            // 预处理任务
            EnvCallable task = new EnvCallable(env);
            // 执行任务，任务结果向后传递，A=1,B=2+A,C=3+B（C=3+2+1）依次类推
            List<ListenableFuture<Dict>> list = Lists.newArrayList(Async.submit(task));
            for (int i = 0; i < model.getPlans().size(); i++) {
                JSON plans = model.getPlans().get(i);
                if (i == 0) {
                    list.add(future(i, plans, list.get(0), context));
                } else {
                    list.add(future(i, plans, list.get(i), context));
                }
            }
            // 最后任务
            ListenableFuture<Dict> future = list.get(list.size() - 1);
            // 同步执行
            Dict dict = future.get();
            // 尝试获取当前执行环境中，是否存储错误信息
            if (ObjectUtil.isEmpty(MapUtil.getAny(dict, ActuatorContext.FIELDS.error))) {
                context.put(dict);
                callback.onSuccess(context.get(), dict);
            } else {
                Result r = MapUtil.get(dict, ActuatorContext.FIELDS.error, Result.class);
                callback.onFailure(context.get(), ExceptionUtil.wrapRuntime(r.msg()));
            }
        } catch (Exception e) {
            callback.onFailure(context.get(), ExceptionUtil.wrapRuntime(e));
        }
        log.debug(">>>调度结束 {}\r\n", env.getTrace());
    }

    private ListenableFuture<Dict> future(Integer idx, JSON plans, ListenableFuture<Dict> input, ActuatorContext current) {
        return Futures.transform(input, new PlanPerformFunction(idx, plans, current), Async.getExecutorService());
    }
}
