package com.xiesx.fastboot.base.page;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.test.base.BaseMock;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class PResultTest extends BaseMock {

  PResult result;

  @BeforeEach
  void setup() {
    result =
        PResult.builder()
            .code(200)
            .msg("OK")
            .data(Arrays.asList("item1", "item2"))
            .count(2)
            .build();
  }

  // 1. getStatus 方法（依赖 isSuccess）
  @Test
  @Order(1)
  void testGetStatusTrueViaIsSuccessMock() {
    PResult spyResult = spy(result);
    doReturn(true).when(spyResult).isSuccess();
    assertTrue(spyResult.getStatus());
  }

  // 2. isSuccess 方法
  @Test
  @Order(2)
  void testGetStatusFalseViaIsSuccessMock() {
    PResult spyResult = spy(result);
    doReturn(false).when(spyResult).isSuccess();
    assertFalse(spyResult.getStatus());
  }

  @Test
  @Order(3)
  void testIsSuccessTrue() {
    PResult result = PResult.builder().code(R.SUCCESS_CODE).build();
    assertTrue(result.isSuccess());
  }

  @Test
  @Order(4)
  void testIsSuccessFalse() {
    PResult result = PResult.builder().code(-1).build();
    assertFalse(result.isSuccess());
  }
}
