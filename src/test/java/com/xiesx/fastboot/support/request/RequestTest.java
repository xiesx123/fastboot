package com.xiesx.fastboot.support.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.support.retryer.AttemptTimeLimiters;
import com.xiesx.fastboot.support.retryer.Retryer;
import com.xiesx.fastboot.support.retryer.RetryerBuilder;
import com.xiesx.fastboot.support.retryer.StopStrategies;
import com.xiesx.fastboot.support.retryer.WaitStrategies;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.concurrent.TimeUnit;

@Log4j2
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class RequestTest {

    public static final String URL =
            "https://front-gateway.mtime.cn/ticket/schedule/showing/movies.api?locationId=561";

    @Test
    @Order(1)
    public void request() {
        // 构造请求
        HttpRequest request = HttpRequest.get(URL).timeout(0);
        // 请求重试
        HttpResponse response = HttpRequests.retry(request);
        // 解析结果
        Object lid = R.eval(response.body(), "$.data.lid");
        // 验证结果
        assertEquals(lid, 561);
    }

    @Test
    @Order(2)
    public void retryer() {
        // 构造请求
        HttpRequest request = HttpRequest.get(URL);
        // 自定义重试器
        Retryer<HttpResponse> retryer =
                RetryerBuilder.<HttpResponse>newBuilder()
                        // 重试条件1: 当请求异常时重试
                        .retryIfException()
                        // 重试条件2: 状态码范围在200~299内
                        .retryIfResult(response -> !response.isOk())
                        // 等待策略：请求间隔1s
                        .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                        // 停止策略：尝试请求3次
                        .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                        // 时间限制：请求限制2s
                        .withAttemptTimeLimiter(
                                AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))
                        // 重试监听
                        .withRetryListener(HttpRetryer.reRetryListener)
                        .build();
        // 请求重试
        HttpResponse response = HttpRequests.retry(request, retryer);
        // 解析结果
        Object lid = R.eval(response.body(), "$.data.lid");
        // 验证结果
        assertEquals(lid, 561);
    }
}
