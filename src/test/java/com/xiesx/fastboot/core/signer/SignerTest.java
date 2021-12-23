package com.xiesx.fastboot.core.signer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.base.BaseResult;
import com.xiesx.fastboot.app.base.BaseTest;
import com.xiesx.fastboot.core.signer.configuration.SignerProperties;

import io.restassured.response.Response;

/**
 * @title SignerTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:42
 */
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class SignerTest extends BaseTest {

    @Autowired
    SignerProperties properties;

    Map<String, String> param, header;

    @BeforeEach
    public void befoe() {
        // 参数
        param = Maps.newConcurrentMap();
        param.put("p1", "1");
        param.put("p2", "2");
        // 签名
        String sign = SignerHelper.getSignature(param, properties.getSecret());
        // 头部
        header = Maps.newConcurrentMap();
        header.put(properties.getHeader(), sign);
    }

    @Test
    @Order(1)
    public void sign() {
        Response res = post("/signer/sign", header, param);
        BaseResult<List<Object>> result = JSON.parseObject(res.asString(), tr_B_List);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(result.getData().get(0), "1");
    }

    @Test
    public void ignore() {
        Response res = get("/signer/sign/ignore?p1=1&p2=2");
        BaseResult<List<Object>> result = JSON.parseObject(res.asString(), tr_B_List);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(result.getData().get(0), "1");
    }
}
