package com.xiesx.fastboot.core.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alibaba.fastjson2.JSON;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.test.base.BaseResult;
import com.xiesx.fastboot.test.base.BaseTest;
import com.xiesx.fastboot.test.mock.MockUser;

import io.restassured.response.Response;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class FastJsonControllerTest extends BaseTest {

    @Test
    @Order(1)
    public void desensitized() {
        Response response = get("fastjson/desensitized");
        BaseResult<Object> result = gtbo.parseObject(response.asString());
        MockUser user = JSON.parseObject(result.getData().toString(), MockUser.class);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(user.getTel(), "138****8000");
    }
}
