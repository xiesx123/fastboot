package com.xiesx.fastboot.core.event;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.xiesx.fastboot.test.event.SimpleEvent;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class EventBusHelperTest {

  @Test
  void testConstructor() {
    EventBusHelper cls = new EventBusHelper();
    assertNotNull(cls);
  }

  @Test
  @Order(1)
  public void post() throws InterruptedException {
    EventBusHelper.post(new SimpleEvent("测试2", true));
  }
}
