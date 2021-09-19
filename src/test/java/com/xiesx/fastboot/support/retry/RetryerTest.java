package com.xiesx.fastboot.support.retry;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.alibaba.fastjson.JSON;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.support.request.RequestsHelper;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.log4j.Log4j2;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.RequestBuilder;
import net.dongliu.requests.Requests;

/**
 * @title RetryerTest.java
 * @description
 * @author xiesx
 * @date 2020-8-15 14:09:29
 */
@Log4j2
@TestMethodOrder(OrderAnnotation.class)
public class RetryerTest {

    public final static String URL = "https://front-gateway.mtime.cn/ticket/schedule/showing/movies.api?locationId=561";

    @Test
    @Order(1)
    public void retry() {
        // 构造重试
        Retryer<Result> retry = RetryerBuilder.<Result>newBuilder()
                // 重试条件
                .retryIfException()
                // 返回指定结果时重试
                .retryIfResult((@Nullable Result result) -> {
                    if (ObjectUtil.isNull(result) || result.getCode() == -3) {
                        return true;
                    }
                    return false;
                })
                // 等待策略：每次请求间隔1s
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                // 停止策略 : 尝试请求5次
                .withStopStrategy(StopStrategies.stopAfterAttempt(5))
                // 时间限制 : 请求限制3s
                .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(3, TimeUnit.SECONDS))
                // 数据监听
                .withRetryListener(new RetryListener() {

                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        long number = attempt.getAttemptNumber();
                        long delay = attempt.getDelaySinceFirstAttempt();
                        boolean isError = attempt.hasException();
                        boolean isResult = attempt.hasResult();
                        if (attempt.hasException()) {
                            if (attempt.getExceptionCause().getCause() instanceof RunException) {
                                RunException runException = (RunException) attempt.getExceptionCause().getCause();
                                log.warn("onException causeBy:{} {}", runException.getStatus(), runException.getMessage());
                            } else {
                                log.warn("onException causeBy:{}", attempt.getExceptionCause().toString());
                            }
                        } else if (attempt.hasResult()) {
                            try {
                                V result = attempt.get();
                                if (result instanceof Result) {
                                    log.warn("onRetry number:{} error:{} result:{} statusCode:{} delay:{}", number, isError, isResult, ((Result) result).getCode(), delay);
                                }
                            } catch (ExecutionException e) {
                                log.error("onResult exception:{}", e.getCause().toString());
                                throw new RunException(RunExc.RETRY, "test retry");
                            }
                        }
                    }
                }).build();

        try {
            Result result = retry.call(() -> {
                // 构造请求
                RequestBuilder req = Requests.get(URL);
                // 请求重试
                RawResponse response = RequestsHelper.retry(req);
                // 获取结果
                RetryResponse test = response.readToJson(RetryResponse.class);
                // 验证结果，如果结果正确则返回，错误则重试
                if (test.isSuccess()) {
                    return R.succ(test.getData());
                }
                return R.retry(test.getMsg());
            });
            // 验证结果，如果结果正确则返回，错误则重试
            log.info(JSON.toJSONString(R.succ(result.getData())));
        } catch (ExecutionException | RetryException e) {
            throw new RunException(RunExc.RETRY, "test retry");
        }
    }
}
