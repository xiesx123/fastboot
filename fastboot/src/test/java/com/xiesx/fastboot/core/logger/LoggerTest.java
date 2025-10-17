package com.xiesx.fastboot.core.logger;

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
public class LoggerTest extends BaseTest {

    @Test
    @Order(1)
    public void noprint() {
        get("logger/noprint");
    }

    @Test
    @Order(2)
    public void print() {
        get("logger/print");
    }

    @Test
    @Order(3)
    public void format() {
        get("logger/format");
    }

    @Test
    @Order(4)
    public void storage() {
        get("logger/storage");
    }
}
