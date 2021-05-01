package com.xiesx.fastboot.support.validate;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.base.result.R;

import lombok.extern.log4j.Log4j2;

@Log4j2

@SpringBootTest(classes = FastBootApplication.class)
public class ValidatorTest {

    @Autowired
    Validator validator;

    @Test
    public void verify1() {
        ValidatorVo vo = new ValidatorVo();
        Set<ConstraintViolation<ValidatorVo>> violations = validator.validate(vo, Default.class);
        List<String> message = ValidatorHelper.extractMessage(violations);
        log.info(JSON.toJSONString(R.succ(message)));
    }

    @Test
    public void verify2() {
        ValidatorVo vo = new ValidatorVo();
        try {
            ValidatorHelper.validate(vo);
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
