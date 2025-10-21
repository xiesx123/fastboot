package com.xiesx.fastboot.support.async;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.xiesx.fastboot.base.config.Configed;
import com.xiesx.fastboot.support.async.callback.AsyncFutureCallback;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;

public class Async {

  /** 线程池（监听） */
  private static ListeningExecutorService les = newCachedExecutorService();

  /** 上下文 */
  private static TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();

  // ===============================

  public static ListeningExecutorService newCachedExecutorService() {
    return MoreExecutors.listeningDecorator(
        // 线程池（在使用线程池等会池化复用线程的执行组件情况下传递ThreadLocal值）
        TtlExecutors.getTtlExecutorService(
            /** 缓存性线程池 */
            Executors.newCachedThreadPool(
                r -> {
                  Thread thread = new Thread(r);
                  thread.setName(Configed.FASTBOOT);
                  return thread;
                })));
  }

  public static void initExecutorService() {
    initExecutorService(newCachedExecutorService());
  }

  public static void initExecutorService(ListeningExecutorService listeningExecutorService) {
    les = listeningExecutorService;
  }

  public static ListeningExecutorService getExecutorService() {
    return les;
  }

  public static TransmittableThreadLocal<String> getThreadLocal() {
    return context;
  }

  // ===============================

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
  public static <T> List<Future<T>> invokeAll(List<Callable<T>> callables)
      throws InterruptedException {
    return les.invokeAll(callables);
  }

  public static <T> List<Future<T>> invokeAll(List<Callable<T>> callables, int timeout)
      throws InterruptedException {
    return les.invokeAll(callables, timeout, TimeUnit.SECONDS);
  }

  // ===============================

  /** 停止 */
  public static void shutdown() {
    les.shutdown();
  }

  /** 立即停止 */
  public static void shutdownNow() {
    les.shutdownNow();
  }
}
