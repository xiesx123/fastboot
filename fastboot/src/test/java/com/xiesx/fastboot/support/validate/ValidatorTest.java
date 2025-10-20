package com.xiesx.fastboot.support.validate;

import com.alibaba.fastjson2.JSON;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.support.validate.ValidatorTest.ValidatorVo.A;
import com.xiesx.fastboot.support.validate.ValidatorTest.ValidatorVo.B;
import com.xiesx.fastboot.support.validate.ValidatorTest.ValidatorVo.C;
import com.xiesx.fastboot.support.validate.annotation.VBlank;
import com.xiesx.fastboot.support.validate.annotation.VJson;
import com.xiesx.fastboot.support.validate.annotation.VNumber;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Log4j2
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ValidatorTest {

  @Autowired Validator validator;

  @Data
  static class ValidatorVo {

    @VBlank(message = "昵称不能为空", groups = B.class)
    private String nickname;

    @VNumber(message = "验证码不能为空", groups = C.class)
    private String code;

    @VNumber(message = "手机不能为空", groups = C.class)
    private String phone;

    @VJson(message = "数据格式错误", groups = A.class)
    private String data = "{;}";

    public interface A {}

    public interface B {}

    public interface C {}
  }

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
  public void verify3() {
    ValidatorVo vo = new ValidatorVo();
    try {
      ValidatorHelper.validate(vo, A.class, B.class, C.class);
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
