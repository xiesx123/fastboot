package com.xiesx.fastboot.core.logger;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.base.BaseTest;

/**
 * @title LoggerTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:28
 */
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoggerTest extends BaseTest {

    @Test
    @Order(1)
    public void noprint() {
        get("/logger/nonprint");
    }

    @Test
    @Order(2)
    public void print() {
        get("/logger/print");
    }

    @Test
    @Order(3)
    public void format() {
        get("/logger/format");
    }

    @Test
    @Order(4)
    public void storage() {
        get("/logger/storage");
    }
}
