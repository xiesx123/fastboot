package com.xiesx.fastboot.core.advice;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.base.BaseResult;
import com.xiesx.fastboot.app.base.BaseTest;
import com.xiesx.fastboot.app.mock.MockUser;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import io.restassured.response.Response;

/**
 * @title BodyTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:19:42
 */
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class BodyTest extends BaseTest {

    @Test
    @Order(1)
    public void result() {
        Response res = get("/body/result");
        BaseResult<Map<String, Object>> result = gtBaseMap.parseObject(res.asString());
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(result.getData().get("k1"), "1");
    }

    @Test
    @Order(2)
    public void map() {
        Response res = get("/body/map");
        BaseResult<Map<String, Object>> result = gtBaseMap.parseObject(res.asString());
        Map<String, Object> data = result.getData();
        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals(data.get("k1"), "1");
    }

    @Test
    @Order(3)
    public void list() {
        Response res = get("/body/list");
        BaseResult<List<Object>> result = gtBaseList.parseObject(res.asString());
        List<Object> data = result.getData();
        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals(data.get(0), "k1");
    }

    @Test
    @Order(4)
    public void string() {
        Response res = get("/body/string");
        String result = res.asString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result, "k1");
    }

    @Test
    @Order(5)
    public void fastjson() {
        Response res = get("/body/fastjson");
        BaseResult<JSON> result = gtBaseJson.parseObject(res.asString());
        JSONObject data = (JSONObject) result.getData();
        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals(data.get("k1"), "1");
        assertEquals(data.getJSONArray("list").get(0), "k1");
    }

    @Test
    @Order(6)
    public void object() {
        Response res = get("/body/object");
        BaseResult<Object> result = gtBaseObj.parseObject(res.asString());
        MockUser user = Convert.convert(MockUser.class, result.getData());
        assertNotNull(user);
        assertFalse(ObjectUtil.isEmpty(user));
        assertEquals(user.getTel(), "13800138000");
    }

    @Test
    @Order(7)
    public void ignore() {
        Response res = get("/body/ignore");
        MockUser user = JSON.parseObject(res.asString(), MockUser.class);
        assertNotNull(user);
        assertFalse(ObjectUtil.isEmpty(user));
        assertEquals(user.getTel(), "13800138000");
    }
}
