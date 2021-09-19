package com.xiesx.fastboot.support.executor;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.*;

import cn.hutool.core.collection.ListUtil;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * @title ExecutorHelper.java
 * @description 线程池<br>
 *              newCachedThreadPool：缓存型,先查看池中有没有以前建立的线程，如果有，就reuse；如果没有，就建一个新的线程加入池中。<br>
 *              newFixedThreadPool：固定型,可控制线程最大并发数，超出的线程会在队列中等待。<br>
 *              ScheduledThreadPool 调度型,支持定时及周期性任务执行。<br>
 *              SingleThreadExecutor 单例型,它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。<br>
 * @author xiesx
 * @date 2020-7-21 22:40:11
 */
@Log4j2
public class ExecutorHelper {

    /**
     * 缓存型线程池
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
        if (callback != null) {
            Futures.addCallback(future, callback, MoreExecutors.directExecutor());
        }
        return future;
    }

    /**
     * 提交
     *
     * @param <T>
     * @param task
     * @return
     */
    public static <T> ListenableFuture<T> submit(@NonNull ExecutorTask<T> task) {
        return submit(task, task);
    }

    /**
     * 批量提交
     *
     * @param <T>
     * @param tasks
     * @return
     */
    public static <T> List<Future<T>> invokeAll(@NonNull List<ExecutorTask<T>> tasks) {
        try {
            return les.invokeAll(tasks);
        } catch (InterruptedException e) {
            log.error("executor helper invoke all", e);
        }
        return ListUtil.empty();
    }

    /**
     * 批量提交
     *
     * @param <T>
     * @param tasks
     * @return
     */
    public static <T> List<Future<T>> invokeAll(@NonNull List<ExecutorTask<T>> tasks, int timeout) {
        try {
            return les.invokeAll(tasks, timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("executor helper invoke all", e);
        }
        return ListUtil.empty();
    }

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
