package com.xiesx.fastboot.core.signer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import com.xiesx.fastboot.core.signer.cfg.SignerProperties;

import io.restassured.response.Response;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class SignerTest extends BaseTest {

    @Autowired
    SignerProperties properties;

    Map<String, Object> param, header;

    @BeforeEach
    public void befoe() {
        // 参数
        param = Maps.newHashMap();
        param.put("p1", 1);
        param.put("p2", "2");
        // 签名
        String sign = SignerHelper.getSignature(param, properties.getSecret());
        // 头部
        header = Maps.newHashMap();
        header.put(properties.getHeader(), sign);
    }

    @Test
    public void sign() {
        Response res = post("/signer/sign", header, param);
        BaseResult<List<Object>> result = JSON.parseObject(res.asString(), tr_B_List);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(result.getData().get(0), "1");
    }
 
    @Test
    public void ignore() {
        Response res = get("/signer/ignore?p1=1&p2=2");
        BaseResult<List<Object>> result = JSON.parseObject(res.asString(), tr_B_List);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(result.getData().get(0), "1");
    }
}
