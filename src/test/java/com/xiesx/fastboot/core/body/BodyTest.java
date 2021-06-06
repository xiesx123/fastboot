package com.xiesx.fastboot.core.body;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class BodyTest extends BaseTest {

    @Test
    @Order(1)
    public void result() {
        Response res = get("/body/result");
        BaseResult<Map<String, Object>> result = JSON.parseObject(res.asString(), tr_B_Map);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(result.getData().get("k1"), "1");
    }

    @Test
    @Order(2)
    public void map() {
        Response res = get("/body/map");
        Map<String, Object> result = JSON.parseObject(res.asString(), tr_Map);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.get("k1"), "1");
    }

    @Test
    @Order(3)
    public void list() {
        Response res = get("/body/list");
        List<Object> result = JSON.parseArray(res.asString(), Object.class);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.get(0), "k1");
    }

    @Test
    @Order(4)
    public void json() {
        Response res = get("/body/json");
        JSONObject result = JSON.parseObject(res.asString());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.get("k1"), "1");
        assertEquals(result.getJSONArray("list").get(0), "k1");
    }

    @Test
    @Order(5)
    public void object() {
        Response res = get("/body/object");
        BaseResult<Object> result = JSON.parseObject(res.asString(), tr_B_Obj);
        MockUser user = Convert.convert(MockUser.class, result.getData());
        assertNotNull(user);
        assertFalse(ObjectUtil.isEmpty(user));
        assertEquals(user.getTel(), "13800138000");
    }

    @Test
    @Order(6)
    public void ignore() {
        Response res = get("/body/ignore");
        MockUser user = JSON.parseObject(res.asString(), MockUser.class);
        assertNotNull(user);
        assertFalse(ObjectUtil.isEmpty(user));
        assertEquals(user.getTel(), "13800138000");
    }
}
