package com.xiesx.fastboot.core.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.advice.configuration.AdviceProperties;
import com.xiesx.fastboot.test.base.BaseMock;
import com.xiesx.fastboot.test.mock.MockData;
import com.xiesx.fastboot.test.mock.MockUser;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class GlobalBodyAdviceTest extends BaseMock {

  @Mock ServerHttpRequest mockRequest;

  @Mock ServerHttpResponse mockResponse;

  GlobalBodyAdvice advice;

  @BeforeEach
  void setup() {
    advice = new GlobalBodyAdvice();
  }

  @Test
  @Order(1)
  void testSupportsReturnsFalseWhenMethodIsNull() {
    when(mockParameter.getMethod()).thenReturn(null);

    boolean result = advice.supports(mockParameter, MappingJackson2HttpMessageConverter.class);
    assertFalse(result);
  }

  @Test
  @Order(2)
  void testBeforeBodyWriteReturnsNullWhenReturnTypeIsVoid() {
    // 模拟配置属性
    advice.properties = Mockito.mock(AdviceProperties.class);

    // 模拟不匹配拦截路径
    when(advice.properties.getBodyIgnoresUrls()).thenReturn(Lists.newArrayList("/ignore"));

    // 模拟请求路径不匹配
    URI mockUri = URI.create("/test");
    when(mockRequest.getURI()).thenReturn(mockUri);

    // 模拟方法返回类型为 void
    when(mockParameter.getMethod()).thenReturn(mockMethod);
    when(mockMethod.getReturnType()).thenReturn((Class) Void.TYPE);

    Object result =
        advice.beforeBodyWrite(
            "str",
            mockParameter,
            MediaType.APPLICATION_JSON,
            MappingJackson2HttpMessageConverter.class,
            mockRequest,
            mockResponse);

    assertEquals(result, "str");
  }

  @Test
  @Order(3)
  void testBeforeBodyWriteReturnsNullWhenReturnTypeIsNotVoid() {
    // 模拟配置属性
    advice.properties = Mockito.mock(AdviceProperties.class);

    // 模拟不匹配拦截路径
    when(advice.properties.getBodyIgnoresUrls()).thenReturn(Lists.newArrayList("/ignore"));

    // 模拟请求路径不匹配
    URI mockUri = URI.create("/test");
    when(mockRequest.getURI()).thenReturn(mockUri);

    // 模拟方法返回类型为 String
    when(mockParameter.getMethod()).thenReturn(mockMethod);
    when(mockMethod.getReturnType()).thenReturn((Class) String.class);

    Object result =
        advice.beforeBodyWrite(
            "str",
            mockParameter,
            MediaType.APPLICATION_JSON,
            MappingJackson2HttpMessageConverter.class,
            mockRequest,
            mockResponse);

    assertEquals(result, "str");
  }

  @Test
  @Order(3)
  void testBeforeBodyWriteReturnsNullWhenReturnTypeIsNull() {
    // 模拟配置属性
    advice.properties = Mockito.mock(AdviceProperties.class);

    // 模拟不匹配拦截路径
    when(advice.properties.getBodyIgnoresUrls()).thenReturn(Lists.newArrayList("/ignore"));

    // 模拟请求路径不匹配
    URI mockUri = URI.create("/test");
    when(mockRequest.getURI()).thenReturn(mockUri);

    // 模拟方法返回类型为 obj
    when(mockParameter.getMethod()).thenReturn(mockMethod);
    when(mockMethod.getReturnType()).thenReturn((Class) MockUser.class);

    Object result =
        advice.beforeBodyWrite(
            MockData.user(),
            mockParameter,
            MediaType.APPLICATION_JSON,
            MappingJackson2HttpMessageConverter.class,
            mockRequest,
            mockResponse);

    assertEquals(((Result) result).code(), R.SUCCESS_CODE);
  }

  @Test
  @Order(3)
  void testIsVoidReturnsTrueWhenReturnTypeIsVoid() {
    when(mockParameter.getMethod()).thenReturn(mockMethod);
    when(mockMethod.getReturnType()).thenReturn((Class) Void.TYPE);

    boolean result = advice.isVoid(mockParameter);
    assertTrue(result);
  }

  @Test
  @Order(4)
  void testIsVoidReturnsFalseWhenReturnTypeIsNotVoid() {
    when(mockParameter.getMethod()).thenReturn(mockMethod);
    when(mockMethod.getReturnType()).thenReturn((Class) String.class);

    boolean result = advice.isVoid(mockParameter);
    assertFalse(result);
  }

  @Test
  @Order(5)
  void testIsVoidReturnsFalseWhenMethodIsNull() {
    when(mockParameter.getMethod()).thenReturn(null);

    boolean result = advice.isVoid(mockParameter);
    assertFalse(result);
  }
}
