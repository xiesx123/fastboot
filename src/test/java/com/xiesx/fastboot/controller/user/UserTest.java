package com.xiesx.fastboot.controller.user;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserTest extends BaseTest {

    @Test
    public void body() {
        Response res = get("/body/result");
        BaseResult<List<Object>> result = JSON.parseObject(res.asString(), tr_B_List);
        log.info(result);
        assertTrue(result.isSuccess());
        assertFalse(result.getData().isEmpty());
    }
}
