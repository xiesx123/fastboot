package com.xiesx.fastboot.base.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import com.yomahub.tlog.context.TLogContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class ResultTest {

  Result result;

  @BeforeEach
  void setup() {
    result = new Result().code(200).msg("OK").data("payload");
  }

  // 1. getTrace()
  @Test
  @Order(1)
  void testGetTrace() {
    try (MockedStatic<TLogContext> mocked = mockStatic(TLogContext.class)) {
      mocked.when(TLogContext::getTraceId).thenReturn("TRACE-123");
      assertEquals("TRACE-123", result.getTrace());
    }
  }

  // 2. getStatus() → isSuccess()
  @Test
  @Order(2)
  void testGetStatusTrue() {
    R.SUCCESS_CODE = 0;
    result.code(0);
    assertTrue(result.getStatus());
  }

  @Test
  @Order(3)
  void testGetStatusFalse() {
    R.SUCCESS_CODE = 0;
    result.code(-1);
    assertFalse(result.getStatus());
  }

  @Test
  @Order(4)
  void testIsSuccessTrue() {
    R.SUCCESS_CODE = 0;
    result.code(0);
    assertTrue(result.isSuccess());
  }

  @Test
  @Order(5)
  void testIsSuccessFalse() {
    R.SUCCESS_CODE = 0;
    result.code(-2);
    assertFalse(result.isSuccess());
  }

  @Test
  @Order(6)
  void testIsFailTrue() {
    R.FAIL_CODE = -1;
    result.code(-1);
    assertTrue(result.isFail());
  }

  @Test
  @Order(7)
  void testIsFailFalse() {
    R.FAIL_CODE = -1;
    result.code(0);
    assertFalse(result.isFail());
  }

  @Test
  @Order(8)
  void testIsErrorTrue() {
    R.ERROR_CODE = -2;
    result.code(-2);
    assertTrue(result.isError());
  }

  @Test
  @Order(9)
  void testIsErrorFalse() {
    R.ERROR_CODE = -2;
    result.code(0);
    assertFalse(result.isError());
  }

  @Test
  @Order(10)
  void testIsReTryTrue() {
    R.RETRY_CODE = -3;
    result.code(-3);
    assertTrue(result.isReTry());
  }

  @Test
  @Order(11)
  void testIsReTryFalse() {
    R.RETRY_CODE = -3;
    result.code(0);
    assertFalse(result.isReTry());
  }

  // 7. toJsonString()
  @Test
  @Order(12)
  void testToJsonString() {
    try (MockedStatic<R> mocked = mockStatic(R.class)) {
      mocked.when(() -> R.toJsonStr(result)).thenReturn("{\"code\":200}");
      assertEquals("{\"code\":200}", result.toJsonString());
    }
  }

  // 8. toJsonPrettyStr()
  @Test
  @Order(13)
  void testToJsonPrettyStr() {
    try (MockedStatic<R> mocked = mockStatic(R.class)) {
      mocked.when(() -> R.toJsonPrettyStr(result)).thenReturn("{\n  \"code\": 200\n}");
      assertTrue(result.toJsonPrettyStr().contains("\"code\": 200"));
    }
  }
}
