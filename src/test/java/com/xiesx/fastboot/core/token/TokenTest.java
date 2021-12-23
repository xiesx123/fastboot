package com.xiesx.fastboot.core.token;

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
import com.xiesx.fastboot.base.config.Configed;
import com.xiesx.fastboot.core.token.configuration.TokenCfg;
import com.xiesx.fastboot.core.token.configuration.TokenProperties;
import com.xiesx.fastboot.core.token.header.HttpHeaderParams;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTPayload;
import io.restassured.response.Response;

/**
 * @title TokenTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:50
 */
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TokenTest extends BaseTest {

    @Autowired
    TokenProperties properties;

    static String token = "";

    Map<String, String> param, header;

    @BeforeEach
    public void befoe() {
        // 生成token
        jwt();
        // 参数
        param = Maps.newConcurrentMap();
        param.put("name", "fasotboot");
        param.put("p1", "1");
        // 头部
        header = Maps.newConcurrentMap();
        header.put(properties.getHeader(), token);
    }

    @Test
    @Order(1)
    public void header() {
        Response res = post("/token/header", header, param);
        BaseResult<List<Object>> result = JSON.parseObject(res.asString(), tr_B_List);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(result.getData().get(0), "fasotboot");
        assertEquals(result.getData().get(1), "123");
        //
        HttpHeaderParams hp = JSON.parseObject(result.getData().get(2).toString(), HttpHeaderParams.class);
        assertEquals(hp.getUid(), "123");
    }

    public static void jwt() {
        //
        Map<String, Object> headers = Maps.newConcurrentMap();
        headers.put("user", "xxx");
        //
        Map<String, Object> payload = Maps.newConcurrentMap();
        payload.put(TokenCfg.UID, "123");
        //
        // token = simple(Configed.FASTBOOT, "api");
        // token = simple(Configed.FASTBOOT, "api", JWT_EXPIRE_M_1);
        // token = simple(Configed.FASTBOOT, "api", claims, JWT_EXPIRE_M_1);
        token = JwtHelper.simple(Configed.FASTBOOT, "api", headers, payload, JwtHelper.JWT_EXPIRE_D_1);
        System.out.println(token);
        //
        JWT jwt = JwtHelper.parser(token);
        System.out.println("签名算法：" + jwt.getSigner().getAlgorithm());
        //
        JWTHeader jh = jwt.getHeader();
        System.out.println("头部信息：" + jh.getClaimsJson());
        //
        JWTPayload jp = jwt.getPayload();
        System.out.println("负载信息：" + jp.getClaimsJson());
    }

    public static void main(String[] args) {
        jwt();
    }
}
