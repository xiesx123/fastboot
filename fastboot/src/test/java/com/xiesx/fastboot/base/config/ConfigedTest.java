package com.xiesx.fastboot.base.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.xiesx.fastboot.base.config.Configed.Ordered;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class ConfigedTest {

  @Test
  @Order(1)
  void testConstructorConfiged() {
    Configed cls = new Configed();
    assertNotNull(cls);
  }

  @Test
  @Order(2)
  void testConstructorOrdered() {
    Ordered cls = new Ordered();
    assertNotNull(cls);
  }
}
