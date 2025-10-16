package com.xiesx.fastboot.core.exception;

import static org.junit.jupiter.api.Assertions.*;

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
public class ExceptionTest extends BaseTest {

    @Test
    @Order(1)
    public void runtime() {
        Response response = get("exception/runtime");
        BaseResult<Object> result = gtbo.parseObject(response.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.RUNTIME.getCode());
    }

    @Test
    @Order(2)
    public void request() {
        Response response = get("exception/request");
        BaseResult<Object> result = gtbo.parseObject(response.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.REQUEST.getCode());
    }

    @Test
    @Order(3)
    public void validator_hibernate() {
        Response response = get("exception/hibernate/validator");
        BaseResult<Object> result = gtbo.parseObject(response.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.VALIDATOR.getCode());
    }

    @Test
    @Order(4)
    public void validator_spring() {
        Response response = get("exception/spring/validator");
        BaseResult<Object> result = gtbo.parseObject(response.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.VALIDATOR.getCode());
    }

    @Test
    @Order(5)
    public void database() {
        Response response = get("exception/database");
        BaseResult<Object> result = gtbo.parseObject(response.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.DBASE.getCode());
    }

    @Test
    @Order(6)
    public void custom() {
        Response response = get("exception/custom");
        BaseResult<Object> result = gtbo.parseObject(response.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.SIGN.getCode());
    }
}
