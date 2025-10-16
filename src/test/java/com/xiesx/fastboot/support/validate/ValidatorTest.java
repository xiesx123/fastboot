package com.xiesx.fastboot.support.validate;

import com.alibaba.fastjson2.JSON;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.support.validate.ValidatorVo.TestVoValid.B;
import com.xiesx.fastboot.support.validate.ValidatorVo.TestVoValid.C;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Log4j2
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ValidatorTest {

    @Autowired Validator validator;

    @Test
    @Order(1)
    public void verify1() {
        ValidatorVo vo = new ValidatorVo();
        Set<ConstraintViolation<ValidatorVo>> violations = validator.validate(vo, Default.class);
        List<String> message = ValidatorHelper.extractMessage(violations);
        log.info(JSON.toJSONString(R.succ(message)));
    }

    @Test
    @Order(2)
    public void verify2() {
        ValidatorVo vo = new ValidatorVo();
        try {
            ValidatorHelper.validate(vo, B.class, C.class);
        } catch (ConstraintViolationException e) {
            // 打印 messgae
            List<String> message1 = ValidatorHelper.extractMessage(e);
            log.info(JSON.toJSONString(R.succ(message1)));

            // 打印property + messgae
            Map<String, String> message2 = ValidatorHelper.extractPropertyAndMessage(e);
            log.info(JSON.toJSONString(R.succ(message2)));

            // 打印property + messgae
            List<String> message3 = ValidatorHelper.extractPropertyAndMessageAsList(e);
            log.info(JSON.toJSONString(R.succ(message3)));
        }
    }
}
