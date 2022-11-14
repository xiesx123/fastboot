package com.xiesx.fastboot.support.async;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.jboss.logging.MDC;

import com.google.common.util.concurrent.*;
import com.xiesx.fastboot.base.config.Configed;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.support.async.callback.AsyncFutureCallback;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
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
    private static ListeningExecutorService les = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool((Runnable r) -> {
        Thread thread = new Thread(r);
        thread.setName(Configed.FASTBOOT);
        return thread;
    }));

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
        return les.submit(task);// TODO wrap(task, MDC.getMap())
    }

    /**
     * 提交
     *
     * @param <T>
     * @param task
     * @return
     */
    public static <T> ListenableFuture<T> submit(@NonNull Callable<T> task) {
        return les.submit(task);// TODO wrap(task, MDC.getMap())
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
        ListenableFuture<T> future = les.submit(task);// TODO wrap(task, MDC.getMap())
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
    public static <T> ListenableFuture<T> submit(@NonNull AsyncFutureCallback<T> task) {
        return submit(task, task);// TODO wrap(task, MDC.getMap())
    }

    /**
     * 批量提交
     *
     * @param <T>
     * @param tasks
     * @return
     */
    public static <T> List<Future<T>> invokeAll(@NonNull List<Callable<T>> tasks) {
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
    public static <T> List<Future<T>> invokeAll(@NonNull List<Callable<T>> tasks, int timeout) {
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


    /**
     * 封装任务，加入TraceId，无返回值
     *
     * @param runnable
     * @param mdcContext
     * @return
     */
    public static Runnable wrap(final Runnable runnable, final Map<String, Object> mdcContext) {
        return () -> {
            mdcContext.forEach((k, v) -> {
                MDC.put(k, v);
            });
            MDC.put(Configed.TRACEID, ObjUtil.defaultIfNull(MDC.get(Configed.TRACEID), IdUtil.nanoId(6)));
            runnable.run();
        };
    }

    /**
     * 封装任务，加入TraceId，有返回值
     *
     * @param callable
     * @param mdcContext
     * @param <T>
     * @return
     */
    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, Object> mdcContext) {
        return () -> {
            mdcContext.forEach((k, v) -> {
                MDC.put(k, v);
            });
            MDC.put(Configed.TRACEID, ObjUtil.defaultIfNull(MDC.get(Configed.TRACEID), IdUtil.nanoId(6)));
            return callable.call();
        };
    }
}
