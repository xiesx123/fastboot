package com.xiesx.fastboot.core.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

import com.alibaba.fastjson2.JSON;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.base.config.Configed;
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

import java.util.List;
import java.util.Map;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class BodyTest extends BaseTest {

    @Test
    @Order(1)
    public void result() {
        Response response = get("body/result");
        BaseResult<Map<String, Object>> result = gtbm.parseObject(response.asString());
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(result.getData().get("k1"), "1");
    }

    @Test
    @Order(2)
    public void page() {
        Response response = get("body/page");
        BaseResult<List<Object>> result = gtbl.parseObject(response.asString());
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertTrue(result.getData().size() > 1);
    }

    @Test
    @Order(3)
    public void map() {
        Response response = get("body/map");
        Map<String, Object> result = gtm.parseObject(response.asString());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.get("k1"), "1");
    }

    @Test
    @Order(4)
    public void list() {
        Response response = get("body/list");
        List<Object> result = gtl.parseObject(response.asString());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.get(0), "k1");
    }

    @Test
    @Order(5)
    public void string() {
        Response response = get("body/string");
        String result = response.asString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result, Configed.FASTBOOT);
    }

    @Test
    @Order(6)
    public void object() {
        Response response = get("body/object");
        BaseResult<Object> result = gtbo.parseObject(response.asString());
        MockUser user = Convert.convert(MockUser.class, result.getData());
        assertNotNull(user);
        assertFalse(ObjectUtil.isEmpty(user));
        assertEquals(user.getTel(), "138****8000");
    }

    @Test
    @Order(7)
    public void ignore() {
        Response response = get("body/ignore");
        MockUser user = JSON.parseObject(response.asString(), MockUser.class);
        assertNotNull(user);
        assertFalse(ObjectUtil.isEmpty(user));
        assertEquals(user.getTel(), "138****8000");
    }

    @Test
    @Order(8)
    public void ignore_annotation() {
        Response response = get("body/ignore/annotation");
        MockUser user = JSON.parseObject(response.asString(), MockUser.class);
        assertNotNull(user);
        assertFalse(ObjectUtil.isEmpty(user));
        assertEquals(user.getTel(), "138****8000");
    }
}
