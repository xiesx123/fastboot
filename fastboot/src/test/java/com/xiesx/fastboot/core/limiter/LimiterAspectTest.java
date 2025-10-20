package com.xiesx.fastboot.core.limiter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.xiesx.fastboot.test.base.BaseMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class LimiterAspectTest extends BaseMock {

  LimiterAspect cls;

  @BeforeEach
  void setup() throws Exception {
    cls = new LimiterAspect();
  }

  @Test
  void testConstructor() {
    cls.limiterPointcut();
    assertNotNull(cls);
  }
}
