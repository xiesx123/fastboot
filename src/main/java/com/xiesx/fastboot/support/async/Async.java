package com.xiesx.fastboot.support.async;

import java.util.List;
import java.util.concurrent.*;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.util.concurrent.*;
import com.xiesx.fastboot.base.config.Configed;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.support.async.callback.AsyncFutureCallback;
import com.yomahub.tlog.core.thread.TLogInheritableTask;

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
    public static ExecutorService es = Executors.newCachedThreadPool(r -> {
        Thread thread = new Thread(r);
        thread.setName(Configed.FASTBOOT);
        return thread;
    });

    /**
     * 线程池（在使用线程池等会池化复用线程的执行组件情况下传递ThreadLocal值）
     */
    public static ExecutorService ttles = TtlExecutors.getTtlExecutorService(es);

    /**
     * 线程池（监听）
     */
    public static ListeningExecutorService les = MoreExecutors.listeningDecorator(ttles);

    /**
     * 上下文
     */
    public static TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();

    // ===============================

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
        return les.submit(wrap(task));
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
    public static <T> ListenableFuture<T> submit(@NonNull AsyncFutureCallback<T> task) {
        return submit(task, task);
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

    // ===============================

    public static Runnable wrap(final Runnable runnable) {
        return new TLogInheritableTask() {

            @Override
            public void runTask() {
                runnable.run();
            }
        };
    }
}
