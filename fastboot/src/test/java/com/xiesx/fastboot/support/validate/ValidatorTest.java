package com.xiesx.fastboot.support.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
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

  ValidatorHelper cls;

  @BeforeEach
  void setup() {
    cls = new ValidatorHelper();
  }

  @Test
  void testConstructor() {
    assertNotNull(cls);
  }

  @Test
  @Order(1)
  public void validate() {
    ValidatorVo vo = new ValidatorVo();
    ValidatorHelper.validate(vo);
    Set<ConstraintViolation<ValidatorVo>> violations = validator.validate(vo);
    List<String> message = ValidatorHelper.extractMessage(violations);
    assertEquals(message.size(), 0);
  }

  @Test
  @Order(2)
  public void extract() {
    ValidatorVo vo = new ValidatorVo();
    try {
      ValidatorHelper.validate(vo, A.class, B.class, C.class);
    } catch (ConstraintViolationException e) {
      // 打印 messgae
      List<String> message1 = ValidatorHelper.extractMessage(e.getConstraintViolations());
      assertEquals(message1.size(), 1);

      // 打印property + messgae
      Map<String, String> message2 =
          ValidatorHelper.extractPropertyAndMessage(e.getConstraintViolations());
      assertEquals(message2.size(), 1);

      // 打印property + messgae
      List<String> message3 =
          ValidatorHelper.extractPropertyAndMessageAsList(e.getConstraintViolations());
      log.info(JSON.toJSONString(R.succ(message3)));
      assertEquals(message3.size(), 1);
    }
  }
}
