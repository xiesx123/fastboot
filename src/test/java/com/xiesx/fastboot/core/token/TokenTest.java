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
import com.xiesx.fastboot.core.token.cfg.TokenCfg;
import com.xiesx.fastboot.core.token.cfg.TokenProperties;
import com.xiesx.fastboot.core.token.header.HeaderParams;

import cn.hutool.core.date.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
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

    String token = "";

    Map<String, Object> param, header;

    @BeforeEach
    public void befoe() {
        // 生成token
        jwt();
        // 参数
        param = Maps.newConcurrentMap();
        param.put("name", "fasotboot");
        param.put("p1", 1);
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
        HeaderParams hp = JSON.parseObject(result.getData().get(2).toString(), HeaderParams.class);
        assertEquals(hp.getUid(), "123");
    }

    public void jwt() {
        //
        Map<String, Object> header = Maps.newConcurrentMap();
        header.put("user", "xxx");
        //
        Map<String, Object> claims = Maps.newConcurrentMap();
        claims.put(TokenCfg.UID, "123");
        //
        // token = simple(Configed.FASTBOOT, "api");
        // token = simple(Configed.FASTBOOT, "api", JWT_EXPIRE_M_1);
        // token = simple(Configed.FASTBOOT, "api", claims, JWT_EXPIRE_M_1);
        token = JwtHelper.simple(Configed.FASTBOOT, "api", header, claims, JwtHelper.JWT_EXPIRE_D_1);
        System.out.println(token);
        //
        Jws<Claims> jws = JwtHelper.parser(token);
        System.out.println("签名信息：" + jws.getSignature());
        //
        JwsHeader<?> h = jws.getHeader();
        System.out.println("头部信息：" + h.getOrDefault("user", "-"));
        //
        Claims c = jws.getBody();
        System.out.println("用户id：" + c.getId());
        System.out.println("主题：" + c.getSubject());
        System.out.println("签发者：" + c.getIssuer());
        System.out.println("接收者：" + c.getAudience());
        System.out.println("登录时间：" + DateUtil.formatDateTime(c.getIssuedAt()));
        System.out.println("过期时间：" + DateUtil.formatDateTime(c.getExpiration()));
        System.out.println("是否过期时间：" + JwtHelper.isExpired(c.getExpiration()));
        System.out.println("附加编号：" + c.getOrDefault(TokenCfg.UID, "-"));
    }
}
