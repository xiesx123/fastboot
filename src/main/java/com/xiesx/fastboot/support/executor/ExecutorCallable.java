package com.xiesx.fastboot.support.executor;

import java.util.concurrent.Callable;

import lombok.extern.log4j.Log4j2;

/**
 * @title ExecutorTask.java
 * @description 基类任务：线程池，基类任务，定义3种状态
 * @author xiesx
 * @date 2020-7-21 22:39:54
 */
@Log4j2
public class ExecutorCallable<T> implements Callable<T> {

    /**
     * 执行
     */
    @Override
    public T call() throws Exception {
        log.debug("executor call.......");
        return null;
    }
}
