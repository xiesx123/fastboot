package com.xiesx.fastboot.support.async.callback;

import com.google.common.util.concurrent.FutureCallback;

import lombok.extern.log4j.Log4j2;

import org.jspecify.annotations.Nullable;

import java.util.concurrent.Callable;

@Log4j2
public class AsyncFutureCallback<T> implements Callable<T>, FutureCallback<T> {

    /** 执行 */
    @Override
    public T call() throws Exception {
        log.trace("executor task call.......");
        return null;
    }

    /** 成功 */
    @Override
    public void onSuccess(@Nullable T t) {
        log.trace("executor task call success");
    }

    /** 失败 */
    @Override
    public void onFailure(@Nullable Throwable e) {
        log.error("executor task call fail", e);
    }
}
