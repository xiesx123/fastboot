# 异步增强

- 使用 `ListenableFuture` 监听任务，当完成时可以得到计算结果
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
        // 这里进行耗时异步运算
        log.info(result);
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

```java [MyTask.java]
@AllArgsConstructor
public static class MyTask extends DefaultTask<Result> {

    public String keyword;

    @Override
    public Result call() throws Exception {
        // 这里进行耗时异步运算
        return R.succ(keyword);
    }

    @Override
    public void onSuccess(Result result) {
        log.info(result.getMsg());
    }

    @Override
    public void onFailure(Throwable t) {
        log.info(t.getMessage());
    }
}
```

:::

```java
@Test
public void executor() throws InterruptedException, ExecutionException {
    // 执行任务，不监听结果
    ExecutorHelper.submit(new MyRunnable("1"));

    // 执行任务，监听结果1
    ExecutorHelper.submit(new Callable<Result>() {

        @Override
        public Result call() throws Exception {
            // 这里进行耗时异步运算
            return R.succ("2");
        }

    }, new MyFutureCallback());

    // 执行任务，监听结果2
    ExecutorHelper.submit(new MyTask("3"));

    // 执行任务
    ListenableFuture<String> future1 = ExecutorHelper.submit(() -> "4");
    // 处理结果
    ListenableFuture<String> future2 = Futures.transform(future1, new Function<String, String>() {

        @Override
        public String apply(String input) {
            return input + " transform";
        }
    }, MoreExecutors.directExecutor());
    // 监听结果
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
```

```log
[FastBoot][ INFO][08-22 16:24:49]-->[pool-2-thread-1: 2411][run(ExecutorHelperTest.java:64)] | - 1
[FastBoot][ INFO][08-22 16:24:49]-->[main: 2421][onSuccess(ExecutorHelperTest.java:72)] | - 2
[FastBoot][ INFO][08-22 16:24:49]-->[pool-2-thread-1: 2426][onSuccess(ExecutorHelperTest.java:94)] | - 3
[FastBoot][ INFO][08-22 16:24:49]-->[main: 2432][onSuccess(ExecutorHelperTest.java:43)] | - 4 transform
```
