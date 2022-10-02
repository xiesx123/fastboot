package com.xiesx.fastboot.support.request;

import java.util.concurrent.TimeUnit;

import org.jboss.logging.MDC;

import com.xiesx.fastboot.base.config.Configed;
import com.xiesx.fastboot.support.retry.*;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.NonNull;
import net.dongliu.requests.Methods;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.RequestBuilder;
import net.dongliu.requests.json.FastJsonProcessor;
import net.dongliu.requests.json.JsonLookup;

/**
 * @title Requests.java
 * @description
 * @author xiesx
 * @date 2022-02-25 11:28:15
 */
public class Requests {

    private static Retryer<RawResponse> retry;

    static {
        // 默认网络转化器
        JsonLookup.getInstance().register(new FastJsonProcessor());
        // 默认网络重试器
        retry = RetryerBuilder.<RawResponse>newBuilder()
                // 重试条件
                .retryIfException()
                // 返回指定结果时重试
                .retryIfResult(HttpRetryer.reRetryPredicate)
                // 等待策略：请求间隔1s
                .withWaitStrategy(WaitStrategies.fixedWait(HttpRetryer.RETRY_HTTP_WAIT, TimeUnit.SECONDS))
                // 停止策略：尝试请求3次
                .withStopStrategy(StopStrategies.stopAfterAttempt(HttpRetryer.RETRY_HTTP_NUM))
                // 时间限制：请求限制2s
                .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(HttpRetryer.RETRY_HTTP_LIMIT, TimeUnit.SECONDS))
                // 重试监听
                .withRetryListener(HttpRetryer.reRetryListener)
                //
                .build();
    }

    public static Retryer<RawResponse> getRetry() {
        return retry;
    }

    public static RawResponse retry(@NonNull RequestBuilder request) {
        return HttpRetryer.retry(request, retry);
    }

    public static RawResponse retry(@NonNull RequestBuilder request, @NonNull Retryer<RawResponse> retry) {
        return HttpRetryer.retry(request, retry);
    }

    // ====================

    public static RequestBuilder get(String url) {
        MDC.put(Configed.TRACEID, ObjUtil.defaultIfNull(MDC.get(Configed.TRACEID), IdUtil.nanoId()));
        return net.dongliu.requests.Requests.newRequest(Methods.GET, url).headers(MDC.getMap());
    }

    public static RequestBuilder post(String url) {
        MDC.put(Configed.TRACEID, ObjUtil.defaultIfNull(MDC.get(Configed.TRACEID), IdUtil.nanoId()));
        return net.dongliu.requests.Requests.newRequest(Methods.POST, url).headers(MDC.getMap());
    }

    public static RequestBuilder delete(String url) {
        MDC.put(Configed.TRACEID, ObjUtil.defaultIfNull(MDC.get(Configed.TRACEID), IdUtil.nanoId()));
        return net.dongliu.requests.Requests.newRequest(Methods.DELETE, url).headers(MDC.getMap());
    }
}
