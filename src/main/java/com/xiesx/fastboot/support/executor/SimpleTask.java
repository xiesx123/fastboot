package com.xiesx.fastboot.support.executor;

import java.util.concurrent.Callable;

import com.google.common.util.concurrent.FutureCallback;

import lombok.extern.log4j.Log4j2;

/**
 * @title SimpleTask.java
 * @description 基类任务：线程池，基类任务，定义3种状态
 * @author xiesx
 * @date 2020-7-21 22:39:54
 */
@Log4j2
public class SimpleTask<T> implements Callable<T>, FutureCallback<T> {

    /**
     * 执行
     */
    @Override
    public T call() throws Exception {
        log.debug("task call.......");
        return null;
    }

    /**
     * 成功
     */
    @Override
    public void onSuccess(T t) {
        log.debug("task call success");
    }

    /**
     * 失败
     */
    @Override
    public void onFailure(Throwable e) {
        log.error("task call fail", e);
    }
}
