package com.xiesx.fastboot.support.retryer;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import com.google.common.base.Objects;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.support.request.HttpRequests;
import com.xiesx.fastboot.support.request.HttpRetryer;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Log4j2
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class RetryerTest {

    public static final String URL =
            "https://front-gateway.mtime.cn/ticket/schedule/showing/movies.api?locationId=563";

    @Test
    @Order(1)
    public void retry() {
        // 业务处理重试
        Retryer<Result> retry =
                RetryerBuilder.<Result>newBuilder()
                        // 重试条件
                        .retryIfException()
                        // 返回指定结果时重试
                        .retryIfResult(
                                (Result result) -> ObjectUtil.isNull(result) || result.code() == -1)
                        // 等待策略：每次请求间隔1s
                        .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                        // 停止策略 : 尝试请求3次
                        .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                        // 时间限制 : 请求限制2s
                        .withAttemptTimeLimiter(
                                AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))
                        // 数据监听
                        .withRetryListener(HttpRetryer.reRetryListener)
                        .build();

        try {
            Result result =
                    retry.call(
                            () -> {
                                // 构造请求
                                HttpRequest req = HttpRequest.get(URL);
                                // 网络请求重试(retryA)
                                HttpResponse response = HttpRequests.retry(req);
                                // 获取结果
                                String body = response.body();
                                // 解析结果
                                Object code = R.eval(body, "$.code");
                                String msg = R.eval(body, "$.msg").toString();
                                // 手动限制2秒
                                // ThreadUtil.safeSleep(2000);
                                // 验证结果
                                if (Objects.equal(code, 0)) {
                                    return R.succ(msg, R.eval(body, "$.data"));
                                }
                                return R.fail(msg);
                            });
            // 验证结果，如果结果正确则返回，错误则重试
            log.info(R.eval(result.data(), "$.lid"));
        } catch (ExecutionException e) {
            throw new RunException(RunExc.RUNTIME, "run");
        } catch (RetryException e) {
            throw new RunException(RunExc.RETRY, "retry");
        }
    }
}
