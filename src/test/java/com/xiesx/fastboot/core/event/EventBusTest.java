package com.xiesx.fastboot.core.event;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.base.BaseTest;

/**
 * @title EventBusTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:19:52
 */
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class EventBusTest extends BaseTest {

    @Test
    @Order(1)
    public void request() {
        get("/event/request?p=测试");
    }

    @Test
    @Order(2)
    public void test() throws InterruptedException {
        // 发布Base消息
        EventBusHelper.post(new SimpleEvent("测试1", true));
        // 发布Object消息
        EventBusHelper.post("测试2");
        // 模拟耗时操作，线程暂停3秒
        TimeUnit.SECONDS.sleep(3);
    }
}
