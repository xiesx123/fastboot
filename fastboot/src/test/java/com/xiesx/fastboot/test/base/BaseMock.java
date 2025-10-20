package com.xiesx.fastboot.test.base;

import com.xiesx.fastboot.core.token.configuration.TokenProperties;
import com.xiesx.fastboot.core.token.interceptor.TokenInterceptor;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;

public class BaseMock {

  // springframework
  public @Mock ApplicationContext mockContext;

  public @Mock ContextRefreshedEvent mockEvent;

  public @Mock MethodParameter mockParameter;

  public @Mock Method mockMethod;

  public @Mock Model mockModel;

  public @Mock HandlerMethod handlerMethod;

  public @Mock RequestAttributes mockAttributes;

  // aspectj
  public @Mock MethodSignature mockSignature;

  public @Mock ProceedingJoinPoint mockPjp;

  // servlet
  public @Mock HttpServletRequest mockRequest;

  public @Mock HttpServletResponse mockResponse;

  public @Mock MultipartFile mockFile;

  // fastboot
  public @Mock TokenProperties mockTokenProperties;

  public @InjectMocks TokenInterceptor moTokenInterceptor;
}
