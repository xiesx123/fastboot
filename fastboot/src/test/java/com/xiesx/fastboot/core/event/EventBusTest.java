package com.xiesx.fastboot.core.event;

import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.test.base.BaseTest;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class EventBusTest extends BaseTest {

    @Test
    @Order(1)
    public void request() {
        get("event/post?p=测试1");
    }

    @Test
    @Order(2)
    public void post() throws InterruptedException {
        EventBusHelper.post(new SimpleEvent("测试2", true));
    }
}
