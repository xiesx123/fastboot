package com.xiesx.fastboot.core.fastjson;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alibaba.fastjson.JSON;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.base.BaseResult;
import com.xiesx.fastboot.app.base.BaseTest;
import com.xiesx.fastboot.app.mock.MockUser;
import com.xiesx.fastboot.app.mock.MockUserDesensitized;

import io.restassured.response.Response;

/**
 * @title FastJsonTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:17
 */
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class FastJsonTest extends BaseTest {

    @Test
    @Order(1)
    public void json() {
        Response res = get("/fastjson/json");
        BaseResult<Object> result = JSON.parseObject(res.asString(), tr_B_Obj);
        MockUser user = JSON.parseObject(result.getData().toString(), MockUser.class);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(user.getTel(), "13800138000");
    }

    @Test
    @Order(2)
    public void desensitized() {
        Response res = get("/fastjson/json/desensitized");
        BaseResult<Object> result = JSON.parseObject(res.asString(), tr_B_Obj);
        MockUserDesensitized user = JSON.parseObject(result.getData().toString(), MockUserDesensitized.class);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(user.getTel(), "138****8000");
    }
}
