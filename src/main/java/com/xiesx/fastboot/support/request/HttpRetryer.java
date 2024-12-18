package com.xiesx.fastboot.support.request;

import java.util.concurrent.ExecutionException;

import com.google.common.base.Predicate;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.support.retry.Attempt;
import com.xiesx.fastboot.support.retry.RetryException;
import com.xiesx.fastboot.support.retry.RetryListener;
import com.xiesx.fastboot.support.retry.Retryer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.log4j.Log4j2;

/**
 * @title HttpRetryer.java
 * @description 自定义Requests网络请求类实现类，基于Requests来实现（网络请求 + 动态代理 + 失败重试）
 * @author xiesx
 * @date 2020-7-21 22:42:34
 */
@Log4j2
public class HttpRetryer {

    /**
     * 重试等待
     */
    public static Integer RETRY_HTTP_WAIT = 1;

    /**
     * 重试次数
     */
    public static Integer RETRY_HTTP_NUM = 3;

    /**
     * 重试限制
     */
    public static Integer RETRY_HTTP_LIMIT = 2;

    /**
     * 重试条件
     */
    public static Predicate<HttpResponse> reRetryPredicate = raw -> !raw.isOk();

    /**
     * 重试监听
     */
    public static RetryListener reRetryListener = new RetryListener() {

        @Override
        public <V> void onRetry(Attempt<V> attempt) {
            long number = attempt.getAttemptNumber();
            long delay = attempt.getDelaySinceFirstAttempt();
            boolean isError = attempt.hasException();
            boolean isResult = attempt.hasResult();
            if (attempt.hasException()) {
                if (attempt.getExceptionCause().getCause() instanceof RunException) {
                    RunException runException = (RunException) attempt.getExceptionCause().getCause();
                    log.trace("exception cause by:{} {}", runException.getStatus(), runException.getMessage());
                } else {
                    log.trace("exception cause by:{}", attempt.getExceptionCause().toString());
                }
            } else if (attempt.hasResult()) {
                try {
                    V result = attempt.get();
                    if (result instanceof HttpResponse) {
                        log.trace("retry number:{} error:{} result:{} code:{} delay:{}", number, isError, isResult, ((HttpResponse) result).getStatus(), delay);
                    }
                } catch (ExecutionException e) {
                    log.error("result exception:{}", e.getCause().toString());
                    throw new RunException(RunExc.REQUEST, "http retry error");
                }
            }
        }
    };

    /**
     * 网络重试
     *
     * @param request
     * @param retry
     * @return
     */
    public static HttpResponse retry(HttpRequest request, Retryer<HttpResponse> retry) {
        try {
            return retry.call(() -> request.execute());
        } catch (ExecutionException | RetryException e) {
            throw new RunException(RunExc.REQUEST, "http retry error");
        }
    }
}
