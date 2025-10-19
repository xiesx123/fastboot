package com.xiesx.fastboot.core.limiter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.test.base.BaseResult;
import com.xiesx.fastboot.test.base.BaseTest;

import io.restassured.response.Response;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class LimiterControllerTest extends BaseTest {

    @Test
    @Order(1)
    public void limiter() {
        Response response = get("limiter/limit");
        BaseResult<Object> result = gtbo.parseObject(response.asString());
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }
}
