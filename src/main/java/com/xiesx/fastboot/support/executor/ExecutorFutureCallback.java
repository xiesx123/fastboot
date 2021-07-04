package com.xiesx.fastboot.support.executor;

import com.google.common.util.concurrent.FutureCallback;

import lombok.extern.log4j.Log4j2;

/**
 * @title ExecutorTask.java
 * @description 基类任务：线程池，基类任务，定义3种状态
 * @author xiesx
 * @date 2020-7-21 22:39:54
 */
@Log4j2
public class ExecutorFutureCallback<T> implements FutureCallback<T> {

    /**
     * 成功
     */
    @Override
    public void onSuccess(T t) {
        log.debug("executor future callback success");
    }

    /**
     * 失败
     */
    @Override
    public void onFailure(Throwable e) {
        log.error("executor future callback fail", e);
    }
}
