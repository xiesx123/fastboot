package com.xiesx.fastboot.support.async;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.*;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.support.async.callback.DefaultFutureCallback;

import lombok.NonNull;

/**
 * @title Async.java
 * @description
 * @author xiesx
 * @date 2022-07-23 14:20:33
 */
public class Async {

    /**
     * 缓存性线程池
     */
    private static ListeningExecutorService les = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    /**
     * 获取
     *
     * @return
     */
    public static ListeningExecutorService getExecutorService() {
        return les;
    }

    /**
     * 单个提交
     *
     * @param task
     * @return
     */
    public static ListenableFuture<?> submit(@NonNull Runnable task) {
        return les.submit(task);
    }

    /**
     * 提交
     *
     * @param <T>
     * @param task
     * @return
     */
    public static <T> ListenableFuture<T> submit(@NonNull Callable<T> task) {
        return les.submit(task);
    }

    /**
     * 提交
     *
     * @param <T>
     * @param task
     * @param callback
     * @return
     */
    public static <T> ListenableFuture<T> submit(@NonNull Callable<T> task, @NonNull FutureCallback<T> callback) {
        ListenableFuture<T> future = les.submit(task);
        Futures.addCallback(future, callback, MoreExecutors.directExecutor());
        return future;
    }

    /**
     * 提交
     *
     * @param <T>
     * @param task
     * @return
     */
    public static <T> ListenableFuture<T> submit(@NonNull DefaultFutureCallback<T> task) {
        return submit(task, task);
    }

    /**
     * 批量提交
     *
     * @param <T>
     * @param tasks
     * @return
     */
    public static <T> List<Future<T>> invokeAll(@NonNull List<DefaultFutureCallback<T>> tasks) {
        try {
            return les.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RunException(RunExc.ASYNC, "executor invoke all");
        }
    }

    /**
     * 批量提交
     *
     * @param <T>
     * @param tasks
     * @return
     */
    public static <T> List<Future<T>> invokeAll(@NonNull List<DefaultFutureCallback<T>> tasks, int timeout) {
        try {
            return les.invokeAll(tasks, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RunException(RunExc.ASYNC, "executor invoke all");
        }
    }

    // ===============================

    /**
     * 停止
     */
    public static void shutdown() {
        // shutdown，执行后不再接收新任务，如果里面有任务，就执行完
        les.shutdown();
    }

    /**
     * 立即停止
     */
    public static void shutdownNow() {
        // shutdownNow，执行后不再接受新任务，如果有等待任务，移出队列；有正在执行的，尝试停止service_data.shutdownNow();
        les.shutdownNow();
    }
}
