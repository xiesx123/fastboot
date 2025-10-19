package com.xiesx.fastboot.base.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;

import com.xiesx.fastboot.base.StatusEnum;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class RTest {

    @Test
    void testConstructor() {
        R cls = new R();
        assertNotNull(cls);
    }

    // 1–4. 初始化方法
    @Test
    @Order(1)
    void testInitSuccess() {
        R.initSuccess(201, "OK");
        Result result = R.succ();
        assertEquals(201, result.code());
        assertEquals("OK", result.msg());
    }

    @Test
    @Order(2)
    void testInitFail() {
        R.initFail(401, "FAIL");
        Result result = R.fail();
        assertEquals(401, result.code());
        assertEquals("FAIL", result.msg());
    }

    @Test
    @Order(3)
    void testInitError() {
        R.initError(501, "ERROR");
        Result result = R.error();
        assertEquals(501, result.code());
        assertEquals("ERROR", result.msg());
    }

    @Test
    @Order(4)
    void testInitRetry() {
        R.initRetry(301, "RETRY");
        Result result = R.retry();
        assertEquals(301, result.code());
        assertEquals("RETRY", result.msg());
    }

    // 5–8. 成功方法
    @Test
    @Order(5)
    void testSucc() {
        Result result = R.succ();
        assertNotNull(result);
    }

    @Test
    @Order(6)
    void testSuccWithData() {
        Result result = R.succ(1);
        assertEquals(1, result.data());
    }

    @Test
    @Order(7)
    void testSuccWithMsg() {
        Result result = R.succ("msg");
        assertEquals("msg", result.msg());
    }

    @Test
    @Order(8)
    void testSuccWithMsgAndData() {
        Result result = R.succ("msg", "data");
        assertEquals("msg", result.msg());
        assertEquals("data", result.data());
    }

    // 9–12. 失败方法
    @Test
    @Order(9)
    void testFail() {
        Result result = R.fail();
        assertNotNull(result);
    }

    @Test
    @Order(10)
    void testFailWithData() {
        Result result = R.fail(1);
        assertEquals(1, result.data());
    }

    @Test
    @Order(11)
    void testFailWithMsg() {
        Result result = R.fail("msg");
        assertEquals("msg", result.msg());
    }

    @Test
    @Order(12)
    void testFailWithMsgAndData() {
        Result result = R.fail("msg", "data");
        assertEquals("msg", result.msg());
        assertEquals("data", result.data());
    }

    // 13–16. 异常方法
    @Test
    @Order(13)
    void testError() {
        Result result = R.error();
        assertNotNull(result);
    }

    @Test
    @Order(14)
    void testErrorWithData() {
        Result result = R.error(1);
        assertEquals(1, result.data());
    }

    @Test
    @Order(15)
    void testErrorWithMsg() {
        Result result = R.error("msg");
        assertEquals("msg", result.msg());
    }

    @Test
    @Order(16)
    void testErrorWithMsgAndData() {
        Result result = R.error("msg", "data");
        assertEquals("msg", result.msg());
        assertEquals("data", result.data());
    }

    // 17–20. 重试方法
    @Test
    @Order(17)
    void testRetry() {
        Result result = R.retry();
        assertNotNull(result);
    }

    @Test
    @Order(18)
    void testRetryWithData() {
        Result result = R.retry(1);
        assertEquals(1, result.data());
    }

    @Test
    @Order(19)
    void testRetryWithMsg() {
        Result result = R.retry("msg");
        assertEquals("msg", result.msg());
    }

    @Test
    @Order(20)
    void testRetryWithMsgAndData() {
        Result result = R.retry("msg", "data");
        assertEquals("msg", result.msg());
        assertEquals("data", result.data());
    }

    // 21–23. 构造方法
    @Test
    @Order(21)
    void testBuildWithStatusEnum() {
        Result result = R.build(StatusEnum.SUCCESS);
        assertEquals(StatusEnum.SUCCESS.getCode(), result.code());
        assertEquals(StatusEnum.SUCCESS.getMsg(), result.msg());
    }

    @Test
    @Order(22)
    void testBuildWithCodeAndMsg() {
        Result result = R.build(123, "hello");
        assertEquals(123, result.code());
        assertEquals("hello", result.msg());
    }

    @Test
    @Order(23)
    void testBuildWithCodeMsgData() {
        Result result = R.build(123, "hello", "payload");
        assertEquals("payload", result.data());
    }

    // 24. parse
    @Test
    @Order(24)
    void testParse() {
        Object input = new Object();
        try (MockedStatic<JSONUtil> mocked = mockStatic(JSONUtil.class)) {
            JSON mockJson = Mockito.mock(JSON.class);
            mocked.when(() -> JSONUtil.parse(input, R.jcfg)).thenReturn(mockJson);
            assertEquals(mockJson, R.parse(input));
        }
    }

    // 25–26. toBean
    @Test
    @Order(25)
    void testToBeanFromJson() {
        String json = "{\"code\":123,\"msg\":\"hello\",\"data\":\"world\"}";
        try (MockedStatic<JSONUtil> mocked = mockStatic(JSONUtil.class)) {
            Result mockResult = new Result().code(123).msg("hello").data("world");
            mocked.when(() -> JSONUtil.toBean(json, Result.class)).thenReturn(mockResult);
            Result result = R.toBean(json);
            assertEquals("world", result.data());
        }
    }

    @Test
    @Order(26)
    void testToBeanFromException() {
        Exception ex = new RuntimeException("mock error");
        try (MockedStatic<ExceptionUtil> mocked = mockStatic(ExceptionUtil.class)) {
            mocked.when(() -> ExceptionUtil.getSimpleMessage(ex)).thenReturn("mock error");
            Result result = R.toBean(ex);
            assertEquals("mock error", result.msg());
        }
    }

    // 27–28. eval
    @Test
    @Order(27)
    void testEval() {
        Object input = new Object();
        String expression = "user.name";
        try (MockedStatic<JSONUtil> mocked = mockStatic(JSONUtil.class)) {
            JSON mockJson = mock(JSON.class);
            mocked.when(() -> JSONUtil.parse(input, R.jcfg)).thenReturn(mockJson);
            mocked.when(() -> JSONUtil.getByPath(mockJson, expression)).thenReturn("XIE");
            assertEquals("XIE", R.eval(input, expression));
        }
    }

    @Test
    @Order(28)
    void testEvalWithDefaultValue() {
        Object input = new Object();
        String expression = "user.age";
        try (MockedStatic<JSONUtil> mocked = mockStatic(JSONUtil.class)) {
            JSON mockJson = mock(JSON.class);
            mocked.when(() -> JSONUtil.parse(input, R.jcfg)).thenReturn(mockJson);
            mocked.when(() -> JSONUtil.getByPath(mockJson, expression, 18)).thenReturn(18);
            assertEquals(18, R.eval(input, expression, 18));
        }
    }

    // 29–30. 格式化
    @Test
    @Order(29)
    void testToJsonStr() {
        Object obj = new Result().code(1).msg("ok").data("payload");
        try (MockedStatic<JSONUtil> mocked = mockStatic(JSONUtil.class)) {
            mocked.when(() -> JSONUtil.toJsonStr(obj, R.jcfg)).thenReturn("{\"code\":1}");
            assertEquals("{\"code\":1}", R.toJsonStr(obj));
        }
    }

    @Test
    @Order(30)
    void testToJsonPrettyStr() {
        Object obj = new Result().code(1).msg("ok").data("payload");
        try (MockedStatic<JSONUtil> mocked = mockStatic(JSONUtil.class)) {
            mocked.when(() -> JSONUtil.toJsonPrettyStr(obj)).thenReturn("{\n  \"code\": 1\n}");
            assertTrue(R.toJsonPrettyStr(obj).contains("\"code\": 1"));
        }
    }
}
