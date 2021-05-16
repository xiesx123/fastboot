package com.xiesx.fastboot.core.limiter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alibaba.fastjson.JSON;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.base.BaseResult;
import com.xiesx.fastboot.app.base.BaseTest;

import io.restassured.response.Response;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class LimiterTest extends BaseTest {

    @Test
    public void limiter() {
        Response res = get("/limiter/permits");
        BaseResult<Object> result = JSON.parseObject(res.asString(), tr_B_Obj);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }
}
