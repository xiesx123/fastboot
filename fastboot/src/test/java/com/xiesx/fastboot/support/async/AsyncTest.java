package com.xiesx.fastboot.support.async;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.support.async.callback.AsyncFutureCallback;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Log4j2
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AsyncTest {

  @AllArgsConstructor
  public static class MyRunnable implements Runnable {

    public String result;

    @Override
    public void run() {
      log.info(result);
      // 耗时运算
      ThreadUtil.safeSleep(2000);
    }
  }

  public static class MyFutureCallback implements FutureCallback<Result> {

    @Override
    public void onSuccess(@Nullable Result result) {
      log.info(result.msg());
    }

    @Override
    public void onFailure(@Nullable Throwable t) {
      log.info(t.getMessage());
    }
  }

  @AllArgsConstructor
  public static class MyFutureCallable extends AsyncFutureCallback<Result> {

    public String keyword;

    @Override
    public Result call() throws Exception {
      super.call();
      log.info(keyword);
      if (keyword.contains("error")) {
        throw new RunException(keyword);
      }
      // 耗时运算
      ThreadUtil.safeSleep(2000);
      return R.succ(keyword);
    }

    @Override
    public void onSuccess(@Nullable Result result) {
      super.onSuccess(result);
      log.info(result.msg());
    }

    @Override
    public void onFailure(@Nullable Throwable t) {
      super.onFailure(t);
      log.info(t.getMessage());
    }
  }

  Async cls;

  @BeforeEach
  void setup() {
    cls = new Async();
  }

  @Test
  void testConstructor() {
    assertNotNull(cls);
  }

  @Test
  @Order(1)
  public void executor() {
    // 执行任务,不监听
    Async.submit(new MyRunnable("1"));
    // 执行任务,监听结果
    Async.submit(new MyFutureCallable("2"));
    Async.submit(new MyFutureCallable("3"), new MyFutureCallback());
    Async.submit(() -> R.succ("4"), new MyFutureCallback());
    //
    Async.submit(new MyFutureCallable("error"));
  }

  @Test
  @Order(2)
  public void transform() {
    // 初始任务
    ListenableFuture<Result> future1 = Async.submit(() -> R.succ(4));

    // 处理任务
    ListenableFuture<Integer> future2 =
        Futures.transform(
            future1,
            input -> {
              return input.isSuccess() ? Convert.toInt(input.data(), 0) + 1 : 0;
            },
            MoreExecutors.directExecutor());

    // 监听结果
    Futures.addCallback(
        future2,
        new FutureCallback<Integer>() {

          @Override
          public void onSuccess(@Nullable Integer result) {
            log.info(result);
          }

          @Override
          public void onFailure(@Nullable Throwable t) {
            log.info(t.getMessage());
          }
        },
        Async.getExecutorService());
  }
}
