# 异步增强

基于 [`Guava`](https://github.com/google/guava) 、[`transmittable-thread-local`](https://github.com/alibaba/transmittable-thread-local) 扩展

- 使用 `ListenableFuture` 监听任务，任务完成时可以得到计算结果
- 线程池等复用线程的场景下无法正确传递线程本地变量的问题

## 示例

::: code-group

```java [MyRunnable.java]
public static class MyRunnable implements Runnable {

    public String result;

    public MyRunnable(String result) {
        this.result = result;
    }

    @Override
    public void run() {
        // 耗时运算
        ThreadUtil.safeSleep(2000);
    }
}
```

```java [MyFutureCallback.java]
public static class MyFutureCallback implements FutureCallback<Result> {

    @Override
    public void onSuccess(@Nullable Result result) {
        log.info(result.getMsg());
    }

    @Override
    public void onFailure(Throwable t) {
        log.info(t.getMessage());
    }
}
```

```java [MyFutureCallable.java]
@AllArgsConstructor
public static class MyFutureCallable extends AsyncFutureCallback<Result> {

    public String keyword;

    @Override
    public Result call() throws Exception {
        log.info(keyword);
        // 耗时运算
        ThreadUtil.safeSleep(2000);
        return R.succ(keyword);
    }

    @Override
    public void onSuccess(@Nullable Result result) {
        log.info(result.msg());
    }

    @Override
    public void onFailure(@Nullable Throwable t) {
        log.info(t.getMessage());
    }
}
```

:::

### 基础

```java
@Test
@Order(1)
public void executor() {
    // 执行任务,不监听
    Async.submit(new MyRunnable("1"));
    // 执行任务,监听结果
    Async.submit(new MyFutureCallable("2"));
    Async.submit(new MyFutureCallable("3"), new MyFutureCallback());
    Async.submit(() -> R.succ("4"), new MyFutureCallback());
}
```

```log
2025-10-14 15:18:41 INFO fastboot:3944 AsyncTest.java:39 - 1
2025-10-14 15:18:41 INFO fastboot:3944 AsyncTest.java:65 - 2
2025-10-14 15:18:41 INFO fastboot:3945 AsyncTest.java:65 - 3
2025-10-14 15:18:41 INFO fastboot:3946 AsyncTest.java:49 - 4
```

### 转换

```java
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
```

```log
2025-10-14 15:47:15 INFO fastboot:3854 AsyncTest.java:113 - 5
```
