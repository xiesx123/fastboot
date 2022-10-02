package com.xiesx.fastboot.core.exception;

import static org.junit.jupiter.api.Assertions.*;

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
 * @title ExceptionTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:06
 */
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ExceptionTest extends BaseTest {

    @Test
    @Order(1)
    public void runtime() {
        Response res = get("/exception/runtime");
        BaseResult<Object> result = gtBaseObj.parseObject(res.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.RUNTIME.getCode());
    }

    @Test
    @Order(2)
    public void request() {
        Response res = get("/exception/request");
        BaseResult<Object> result = gtBaseObj.parseObject(res.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.REQUEST.getCode());
    }

    @Test
    @Order(3)
    public void validator_hibernate() {
        Response res = get("/exception/hibernate/validator");
        BaseResult<Object> result = gtBaseObj.parseObject(res.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.VALIDATOR.getCode());
    }

    @Test
    @Order(4)
    public void validator_spring() {
        Response res = get("/exception/spring/validator");
        BaseResult<Object> result = gtBaseObj.parseObject(res.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.VALIDATOR.getCode());
    }

    @Test
    @Order(5)
    public void database() {
        Response res = get("/exception/database");
        BaseResult<Object> result = gtBaseObj.parseObject(res.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.DBASE.getCode());
    }

    @Test
    @Order(6)
    public void custom() {
        Response res = get("/exception/custom");
        BaseResult<Object> result = gtBaseObj.parseObject(res.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.SIGN.getCode());
    }
}
