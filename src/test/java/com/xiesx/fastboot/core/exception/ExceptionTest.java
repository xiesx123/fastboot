package com.xiesx.fastboot.core.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alibaba.fastjson.JSON;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.base.BaseResult;
import com.xiesx.fastboot.app.base.BaseTest;

import io.restassured.response.Response;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ExceptionTest extends BaseTest {

    @Test
    public void runtime() {
        Response res = get("/exception/runtime");
        BaseResult<Object> result = JSON.parseObject(res.asString(), tr_B_Obj);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.RUNTIME.getCode());
    }

    @Test
    public void request() {
        Response res = get("/exception/request");
        BaseResult<Object> result = JSON.parseObject(res.asString(), tr_B_Obj);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.REQUEST.getCode());
    }

    @Test
    public void validator_hibernate() {
        Response res = get("/exception/hibernate/validator");
        BaseResult<Object> result = JSON.parseObject(res.asString(), tr_B_Obj);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.VALIDATOR.getCode());
    }

    @Test
    public void validator_spring() {
        Response res = get("/exception/spring/validator");
        BaseResult<Object> result = JSON.parseObject(res.asString(), tr_B_Obj);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.VALIDATOR.getCode());
    }

    @Test
    public void database() {
        Response res = get("/exception/database");
        BaseResult<Object> result = JSON.parseObject(res.asString(), tr_B_Obj);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.DBASE.getCode());
    }

    @Test
    public void custom() {
        Response res = get("/exception/custom");
        BaseResult<Object> result = JSON.parseObject(res.asString(), tr_B_Obj);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(result.getCode(), RunExc.SIGN.getCode());
    }
}
