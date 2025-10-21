package com.xiesx.fastboot.support.request;

import cn.hutool.http.HttpInterceptor;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xiesx.fastboot.support.retryer.AttemptTimeLimiters;
import com.xiesx.fastboot.support.retryer.Retryer;
import com.xiesx.fastboot.support.retryer.RetryerBuilder;
import com.xiesx.fastboot.support.retryer.StopStrategies;
import com.xiesx.fastboot.support.retryer.WaitStrategies;
import com.yomahub.tlog.hutoolhttp.TLogHutoolhttpInterceptor;
import java.util.concurrent.TimeUnit;
import org.jspecify.annotations.Nullable;

public class HttpRequests {

  private static Retryer<HttpResponse> retry;

  private static HttpInterceptor<HttpRequest> interceptor = new TLogHutoolhttpInterceptor();

  static {
    // 默认网络重试器
    retry =
        RetryerBuilder.<HttpResponse>newBuilder()
            // 重试条件
            .retryIfException()
            // 返回指定结果时重试
            .retryIfResult(res -> !res.isOk())
            // 等待策略：请求间隔1s
            .withWaitStrategy(
                WaitStrategies.fixedWait(HttpRetryer.RETRY_HTTP_WAIT, TimeUnit.SECONDS))
            // 停止策略：尝试请求3次
            .withStopStrategy(StopStrategies.stopAfterAttempt(HttpRetryer.RETRY_HTTP_NUM))
            // 时间限制：请求限制2s
            .withAttemptTimeLimiter(
                AttemptTimeLimiters.fixedTimeLimit(HttpRetryer.RETRY_HTTP_LIMIT, TimeUnit.SECONDS))
            // 重试监听
            .withRetryListener(HttpRetryer.reRetryListener)
            //
            .build();
  }

  public static Retryer<HttpResponse> getDefaultRetry() {
    return retry;
  }

  public static HttpResponse retry(@Nullable HttpRequest request) {
    request.addInterceptor(interceptor);
    return HttpRetryer.retry(request, getDefaultRetry());
  }

  public static HttpResponse retry(
      @Nullable HttpRequest request, @Nullable Retryer<HttpResponse> retry) {
    request.addInterceptor(interceptor);
    return HttpRetryer.retry(request, retry);
  }
}
