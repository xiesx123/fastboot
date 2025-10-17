package com.xiesx.fastboot.support.async;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.util.concurrent.*;
import com.xiesx.fastboot.base.config.Configed;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.support.async.callback.AsyncFutureCallback;

import lombok.NonNull;

import java.util.List;
import java.util.concurrent.*;

public class Async {

    /** 缓存性线程池 */
    public static ExecutorService es =
            Executors.newCachedThreadPool(
                    r -> {
                        Thread thread = new Thread(r);
                        thread.setName(Configed.FASTBOOT);
                        return thread;
                    });

    /** 线程池（在使用线程池等会池化复用线程的执行组件情况下传递ThreadLocal值） */
    public static ExecutorService ttles = TtlExecutors.getTtlExecutorService(es);

    /** 线程池（监听） */
    public static ListeningExecutorService les = MoreExecutors.listeningDecorator(ttles);

    /** 上下文 */
    public static TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();

    // ===============================

    public static ListeningExecutorService getExecutorService() {
        return les;
    }

    /** 提交 */
    public static ListenableFuture<?> submit(Runnable task) {
        return les.submit(task);
    }

    public static <T> ListenableFuture<T> submit(Callable<T> callable) {
        return les.submit(callable);
    }

    public static <T> ListenableFuture<T> submit(
            @NonNull Callable<T> task, FutureCallback<T> callback) {
        ListenableFuture<T> future = les.submit(task);
        Futures.addCallback(future, callback, MoreExecutors.directExecutor());
        return future;
    }

    public static <T> ListenableFuture<T> submit(AsyncFutureCallback<T> callback) {
        return submit(callback, callback);
    }

    /** 批量提交 */
    public static <T> List<Future<T>> invokeAll(List<Callable<T>> callables) {
        try {
            return les.invokeAll(callables);
        } catch (InterruptedException e) {
            throw new RunException(RunExc.ASYNC, "executor invoke all");
        }
    }

    public static <T> List<Future<T>> invokeAll(List<Callable<T>> callables, int timeout) {
        try {
            return les.invokeAll(callables, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RunException(RunExc.ASYNC, "executor invoke all");
        }
    }

    // ===============================

    /** 停止 */
    public static void shutdown() {
        // shutdown，执行后不再接收新任务，如果里面有任务，就执行完
        les.shutdown();
    }

    /** 立即停止 */
    public static void shutdownNow() {
        // shutdownNow，执行后不再接受新任务，如果有等待任务，移出队列；有正在执行的，尝试停止service_data.shutdownNow();
        les.shutdownNow();
    }
}
