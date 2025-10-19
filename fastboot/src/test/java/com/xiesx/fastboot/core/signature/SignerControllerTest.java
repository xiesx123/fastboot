package com.xiesx.fastboot.core.signature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.core.signature.configuration.SignerProperties;
import com.xiesx.fastboot.test.base.BaseResult;
import com.xiesx.fastboot.test.base.BaseTest;

import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.List;
import java.util.Map;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class SignerControllerTest extends BaseTest {

    @Autowired SignerProperties properties;

    Map<String, Object> params, header;

    @BeforeEach
    public void setup() {
        // 参数
        params = Maps.newHashMap();
        params.put("p1", 1);
        params.put("p2", "2");
        params.put("p3", Lists.newArrayList("31", "32"));
        params.put("p4", Lists.newArrayList(41, 42));
        // 签名
        String sign = SignerHelper.getSignature(params, properties.getSecret());
        // 头部
        header = Maps.newHashMap();
        header.put(properties.getHeader(), sign);
    }

    @Test
    @Order(1)
    public void sign() {
        Response response = get("signer/sign", header, params);
        BaseResult<List<Object>> result = gtbl.parseObject(response.asString());
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(result.getData().get(0), "1");
    }

    @Test
    @Order(2)
    public void sign_empty() {
        Response response = get("signer/sign", Maps.newHashMap(), params);
        BaseResult<List<Object>> result = gtbl.parseObject(response.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMsg().contains("token is empty"));
    }

    @Test
    @Order(3)
    public void sign_expired() {
        header.put(properties.getHeader(), "xxx");
        Response response = get("signer/sign", header, params);
        BaseResult<List<Object>> result = gtbl.parseObject(response.asString());
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMsg().contains("token is expired"));
    }

    @Test
    @Order(4)
    public void ignore() {
        Response response = get("signer/sign/ignore", header, params);
        BaseResult<List<Object>> result = gtbl.parseObject(response.asString());
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(result.getData().get(0), "1");
    }

    public static void main(String[] args) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("p1", 1);
        params.put("p2", "2");
        params.put("p3", Lists.newArrayList("31", "32"));
        params.put("p4", new int[] {41, 42});
        String sign = SignerHelper.getSignature(params, "fastboot!@#");
        System.out.println(sign);
        // 4302166d85eedf4139155991d2d183da
    }
}
