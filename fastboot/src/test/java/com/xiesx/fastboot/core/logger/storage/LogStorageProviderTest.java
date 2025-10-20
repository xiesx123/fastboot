package com.xiesx.fastboot.core.logger.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.xiesx.fastboot.test.base.BaseMock;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Log4j2
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class LogStorageProviderTest extends BaseMock {

  @Mock HttpServletRequest mockRequest;

  @Mock HttpServletRequest mockResponse;

  @BeforeEach
  void setup() {}

  @Test
  @Order(1)
  void testGetClientIP() {
    when(mockRequest.getHeader("X-Forwarded-For")).thenReturn("127.0.0.1");
    String ip = LogStorageProvider.getClientIP(mockRequest);
    assertEquals("127.0.0.1", ip);
  }

  @Test
  @Order(2)
  void testGetClientIPByHeader() {
    when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");
    String ip = LogStorageProvider.getClientIPByHeader(mockRequest);
    assertEquals("127.0.0.1", ip);
  }

  @Test
  @Order(3)
  void testGetClientIPByHeaderWithHeaderNames() {
    when(mockRequest.getHeader("x")).thenReturn("127.0.0.1");
    String ip = LogStorageProvider.getClientIPByHeader(mockRequest, "x");
    assertEquals("127.0.0.1", ip);
  }
}
