package com.xiesx.fastboot.core.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.core.lang.Console;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTPayload;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.base.config.Configed;
import com.xiesx.fastboot.core.token.configuration.TokenCfg;
import com.xiesx.fastboot.core.token.configuration.TokenProperties;
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
public class TokenTest extends BaseTest {

    @Autowired TokenProperties properties;

    Map<String, Object> param, header;

    public static String generate() {
        //
        Map<String, Object> headers = Maps.newConcurrentMap();
        headers.put("subscribe", "free");
        //
        Map<String, Object> payload = Maps.newConcurrentMap();
        payload.put(TokenCfg.UID, "1");
        //
        String token =
                JwtHelper.simple(
                        Configed.FASTBOOT, "api", headers, payload, JwtHelper.JWT_EXPIRE_M_1);
        Console.log(token);
        //
        JWT jwt = JwtHelper.parser(token);
        Console.log("签名算法：" + jwt.getSigner().getAlgorithm());
        //
        JWTHeader jh = jwt.getHeader();
        Console.log("头部信息：" + jh.getClaimsJson());
        //
        JWTPayload jp = jwt.getPayload();
        Console.log("负载信息：" + jp.getClaimsJson());

        return token;
    }

    @BeforeEach
    public void befoe() {
        // 参数
        param = Maps.newConcurrentMap();
        param.put("name", "fasotboot");
        // 头部
        header = Maps.newConcurrentMap();
        header.put(properties.getHeader(), generate());
        header.put("h1", 1);
    }

    @Test
    @Order(1)
    public void header() {
        Response response = get("token/header", header, param);
        BaseResult<List<Object>> result = gtbl.parseObject(response.asString());
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(result.getData().get(0), "fasotboot");
        assertEquals(result.getData().get(1), "1");
        assertEquals(result.getData().get(2), "free");
        TokenVo hp = JSON.parseObject(result.getData().get(3).toString(), TokenVo.class);
        assertEquals(hp.getH1(), "1");
    }

    public static void main(String[] args) {
        generate();
    }
}
