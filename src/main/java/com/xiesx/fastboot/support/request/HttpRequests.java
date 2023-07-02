package com.xiesx.fastboot.support.request;

import java.util.concurrent.TimeUnit;

import com.xiesx.fastboot.support.retry.*;
import com.yomahub.tlog.hutoolhttp.TLogHutoolhttpInterceptor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.NonNull;

/**
 * @title Requests.java
 * @description
 * @author xiesx
 * @date 2022-02-25 11:28:15
 */
public class HttpRequests {

    private static Retryer<HttpResponse> retry;

    private static TLogHutoolhttpInterceptor interceptor = new TLogHutoolhttpInterceptor();

    static {
        // 默认网络重试器
        retry = RetryerBuilder.<HttpResponse>newBuilder()
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

    public static Retryer<HttpResponse> getRetry() {
        return retry;
    }

    @SuppressWarnings("unchecked")
    public static HttpResponse retry(@NonNull HttpRequest request) {
        request.addInterceptor(interceptor);
        return HttpRetryer.retry(request, retry);
    }

    @SuppressWarnings("unchecked")
    public static HttpResponse retry(@NonNull HttpRequest request, @NonNull Retryer<HttpResponse> retry) {
        request.addInterceptor(interceptor);
        return HttpRetryer.retry(request, retry);
    }
}
