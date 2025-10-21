package com.xiesx.fastboot.support.request;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cn.hutool.http.HttpRequest;
import com.xiesx.fastboot.core.exception.RunException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Log4j2
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class HttpRetryerTest {

  HttpRetryer cls;

  @BeforeEach
  void setup() {
    cls = new HttpRetryer();
  }

  @Test
  void testConstructor() {
    assertNotNull(cls);
  }

  @Test
  @Order(1)
  public void retryer_error() {
    HttpRequest request = HttpRequest.get("https://www.baidu.com/1");
    assertThrows(RunException.class, () -> HttpRequests.retry(request));
  }
}
