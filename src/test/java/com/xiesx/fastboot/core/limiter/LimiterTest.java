package com.xiesx.fastboot.core.limiter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.base.BaseResult;
import com.xiesx.fastboot.app.base.BaseTest;

import io.restassured.response.Response;

/**
 * @title LimiterTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:22
 */
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class LimiterTest extends BaseTest {

    @Test
    @Order(1)
    public void limiter() {
        Response res = get("/limiter/limit");
        BaseResult<Object> result = gtBaseObj.parseObject(res.asString());
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }
}
