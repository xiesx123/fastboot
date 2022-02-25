package com.xiesx.fastboot.support.executor;

import java.util.concurrent.ExecutionException;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @title ExecutorTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:11
 */
@Log4j2
@TestMethodOrder(OrderAnnotation.class)
public class ExecutorTest {

    @Test
    @Order(1)
    public void executor() throws InterruptedException, ExecutionException {
        // 执行任务,不监听结果
        ExecutorHelper.submit(new MyRunnable("1"));
        // 执行任务,监听结果
        ExecutorHelper.submit(() -> R.succ("2"), new MyFutureCallback());
        // 执行任务,监听结果
        ExecutorHelper.submit(new MyTask("3"));
        // 执行任务,返回结果
        ListenableFuture<String> future1 = ExecutorHelper.submit(() -> "4");
        ListenableFuture<String> future2 = Futures.transform(future1, input -> input + " transform", MoreExecutors.directExecutor());
        Futures.addCallback(future2, new FutureCallback<String>() {

            @Override
            public void onSuccess(@Nullable String result) {
                log.info(result);
            }

            @Override
            public void onFailure(Throwable t) {
                log.info(t.getMessage());
            }
        }, MoreExecutors.directExecutor());
    }

    public static class MyRunnable implements Runnable {

        public String result;

        public MyRunnable(String result) {
            this.result = result;
        }

        @Override
        public void run() {
            // 这里进行耗时异步运算
            log.info(result);
        }
    }

    public static class MyFutureCallback implements FutureCallback<Result> {

        @Override
        public void onSuccess(@Nullable Result result) {
            log.info(result.msg());
        }

        @Override
        public void onFailure(Throwable t) {
            log.info(t.getMessage());
        }
    }

    @AllArgsConstructor
    public static class MyTask extends ExecutorTask<Result> {

        public String keyword;

        @Override
        public Result call() throws Exception {
            // 这里进行耗时异步运算
            return R.succ(keyword);
        }

        @Override
        public void onSuccess(Result result) {
            log.info(result.msg());
        }

        @Override
        public void onFailure(Throwable t) {
            log.info(t.getMessage());
        }
    }
}
