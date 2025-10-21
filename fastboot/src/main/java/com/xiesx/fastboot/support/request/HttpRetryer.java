package com.xiesx.fastboot.support.request;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.support.retryer.Attempt;
import com.xiesx.fastboot.support.retryer.RetryException;
import com.xiesx.fastboot.support.retryer.RetryListener;
import com.xiesx.fastboot.support.retryer.Retryer;
import java.util.concurrent.ExecutionException;
import lombok.Generated;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HttpRetryer {

  /** 重试等待 */
  public static Integer RETRY_HTTP_WAIT = 1;

  /** 重试次数 */
  public static Integer RETRY_HTTP_NUM = 3;

  /** 重试限制 */
  public static Integer RETRY_HTTP_LIMIT = 2;

  /** 重试监听 */
  public static RetryListener reRetryListener =
      new RetryListener() {

        @Generated
        @Override
        public <V> void onRetry(Attempt<V> attempt) {
          long number = attempt.getAttemptNumber();
          long delay = attempt.getDelaySinceFirstAttempt();
          boolean isError = attempt.hasException();
          boolean isResult = attempt.hasResult();
          if (attempt.hasException()) {
            if (attempt.getExceptionCause().getCause() instanceof RunException) {
              RunException runException = (RunException) attempt.getExceptionCause().getCause();
              log.warn(
                  "retry:{} delay:{} exception cause by:{}",
                  number,
                  delay,
                  runException.getStatus(),
                  runException.getMessage());
            } else {
              log.warn(
                  "retry:{} delay:{} exception cause by:{}",
                  number,
                  delay,
                  attempt.getExceptionCause().toString());
            }
          } else if (attempt.hasResult()) {
            try {
              V result = attempt.get();
              if (result instanceof HttpResponse) {
                log.trace(
                    "retry:{} delay:{} error:{} result:{} code:{}",
                    number,
                    delay,
                    isError,
                    isResult,
                    ((HttpResponse) result).getStatus());
              }
            } catch (ExecutionException e) {
              log.error("result exception:{}", e.getCause().toString());
              throw new RunException(RunExc.REQUEST, "http retry error");
            }
          }
        }
      };

  /** 网络重试 */
  public static HttpResponse retry(HttpRequest request, Retryer<HttpResponse> retry) {
    try {
      return retry.call(() -> request.execute());
    } catch (ExecutionException | RetryException e) {
      throw new RunException(RunExc.REQUEST, "http retry error");
    }
  }
}
