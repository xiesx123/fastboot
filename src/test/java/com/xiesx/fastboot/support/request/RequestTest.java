package com.xiesx.fastboot.support.request;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson2.JSON;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.support.retry.RetryResponse;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.log4j.Log4j2;

/**
 * @title RequestTest.java
 * @description
 * @author xiesx
 * @date 2020-8-15 14:09:29
 */
@Log4j2
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class)
public class RequestTest {

    public final static String URL = "https://front-gateway.mtime.cn/ticket/schedule/showing/movies.api?locationId=561";

    @Test
    @Order(1)
    public void http() {
        // 构造请求
        HttpRequest req = HttpRequest.get(URL);
        // 请求重试
        HttpResponse response = HttpRequests.retry(req);
        // 获取结果
        RetryResponse result = JSON.parseObject(response.body(), RetryResponse.class);
        // 验证结果，如果结果正确则返回，错误则重试
        log.info(R.toJsonStr(result.getData()));
    }
}
