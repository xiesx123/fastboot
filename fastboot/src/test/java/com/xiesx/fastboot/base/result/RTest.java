package com.xiesx.fastboot.base.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;

import com.xiesx.fastboot.base.StatusEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RTest {

    @BeforeEach
    void setUp() {
        // Reset the default values before each test
        R.initSuccess(StatusEnum.SUCCESS.getCode(), StatusEnum.SUCCESS.getMsg());
        R.initFail(StatusEnum.FAIL.getCode(), StatusEnum.FAIL.getMsg());
        R.initError(StatusEnum.ERROR.getCode(), StatusEnum.ERROR.getMsg());
        R.initRetry(StatusEnum.RETRY.getCode(), StatusEnum.RETRY.getMsg());
    }

    @Test
    void testInitSuccess() {
        R.initSuccess(200, "Custom Success");
        assertEquals(200, R.SUCCESS_CODE);
        assertEquals("Custom Success", R.SUCCESS_MSG);
    }

    @Test
    void testInitFail() {
        R.initFail(400, "Custom Fail");
        assertEquals(400, R.FAIL_CODE);
        assertEquals("Custom Fail", R.FAIL_MSG);
    }

    @Test
    void testInitError() {
        R.initError(500, "Custom Error");
        assertEquals(500, R.ERROR_CODE);
        assertEquals("Custom Error", R.ERROR_MSG);
    }

    @Test
    void testInitRetry() {
        R.initRetry(300, "Custom Retry");
        assertEquals(300, R.RETRY_CODE);
        assertEquals("Custom Retry", R.RETRY_MSG);
    }

    @Test
    void testSucc() {
        Result result = R.succ();
        assertEquals(R.SUCCESS_CODE, result.code());
        assertEquals(R.SUCCESS_MSG, result.msg());
    }

    @Test
    void testFail() {
        Result result = R.fail();
        assertEquals(R.FAIL_CODE, result.code());
        assertEquals(R.FAIL_MSG, result.msg());
    }

    @Test
    void testError() {
        Result result = R.error();
        assertEquals(R.ERROR_CODE, result.code());
        assertEquals(R.ERROR_MSG, result.msg());
    }

    @Test
    void testRetry() {
        Result result = R.retry();
        assertEquals(R.RETRY_CODE, result.code());
        assertEquals(R.RETRY_MSG, result.msg());
    }

    @Test
    void testBuildWithStatusEnum() {
        Result result = R.build(StatusEnum.SUCCESS);
        assertEquals(StatusEnum.SUCCESS.getCode(), result.code());
        assertEquals(StatusEnum.SUCCESS.getMsg(), result.msg());
    }

    @Test
    void testBuildWithCodeAndMessage() {
        Result result = R.build(201, "Created");
        assertEquals(201, result.code());
        assertEquals("Created", result.msg());
    }

    @Test
    void testBuildWithCodeMessageAndData() {
        String data = "Sample Data";
        Result result = R.build(201, "Created", data);
        assertEquals(201, result.code());
        assertEquals("Created", result.msg());
        assertEquals(data, result.data());
    }

    @Test
    void testParse() {
        String jsonString = "{\"key\":\"value\"}";
        JSON json = R.parse(jsonString);
        assertTrue(JSONUtil.isTypeJSON(json.toString()));
        assertEquals("value", json.getByPath("key"));
    }

    @Test
    void testToBeanFromJson() {
        String jsonString = "{\"code\":200,\"msg\":\"OK\"}";
        Result result = R.toBean(jsonString);
        assertEquals(200, result.code());
        assertEquals("OK", result.msg());
    }

    @Test
    void testToBeanFromException() {
        Exception exception = new Exception("Test Exception");
        Result result = R.toBean(exception);
        assertEquals(R.ERROR_CODE, result.code());
        assertEquals("Test Exception", result.msg());
    }

    @Test
    void testEval() {
        String jsonString = "{\"key\":\"value\"}";
        Object value = R.eval(jsonString, "key");
        assertEquals("value", value);
    }

    @Test
    void testToJsonStr() {
        Result result = R.succ();
        String jsonString = R.toJsonStr(result);
        assertTrue(JSONUtil.isTypeJSON(jsonString));
    }

    @Test
    void testToJsonPrettyStr() {
        Result result = R.succ();
        String jsonString = R.toJsonPrettyStr(result);
        assertTrue(JSONUtil.isTypeJSON(jsonString));
    }
}
