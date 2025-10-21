package com.xiesx.fastboot.core.advice;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.AntPathMatcher;
import com.xiesx.fastboot.base.IStatus;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.core.advice.annotation.RestBodyIgnore;
import com.xiesx.fastboot.core.advice.configuration.AdviceProperties;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Log4j2
@EnableConfigurationProperties(AdviceProperties.class)
@RestControllerAdvice
public class GlobalBodyAdvice implements ResponseBodyAdvice<Object> {

  @Autowired AdviceProperties properties;

  AntPathMatcher match = new AntPathMatcher();

  @Override
  public boolean supports(
      @NonNull MethodParameter returnType,
      @NonNull Class<? extends HttpMessageConverter<?>> converterType) {

    // 获取方法
    Method method = returnType.getMethod();
    if (method == null) {
      return false;
    }
    // 判断类或方法是否有 @RestBodyIgnore 注解
    boolean ignored =
        AnnotationUtil.hasAnnotation(method.getDeclaringClass(), RestBodyIgnore.class)
            || AnnotationUtil.hasAnnotation(method, RestBodyIgnore.class);

    log.trace(
        "{}#{} body write support: {}",
        method.getDeclaringClass().getSimpleName(),
        method.getName(),
        !ignored);

    // true 拦截，false 忽略
    return !ignored;
  }

  @Override
  public Object beforeBodyWrite(
      @Nullable Object body,
      @NonNull MethodParameter returnType,
      @NonNull MediaType selectedContentType,
      @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
      @NonNull ServerHttpRequest request,
      @NonNull ServerHttpResponse response) {

    // 是否需要拦截
    boolean isMatch =
        properties.getBodyIgnoresUrls().stream()
            .anyMatch(i -> match.match(i, request.getURI().getPath()));
    if (isMatch || isVoid(returnType)) {
      return body;
    }
    // 特殊处理
    if (body instanceof Map
        || body instanceof Iterable
        || body instanceof String
        || body instanceof IStatus) {
      return body;
    }
    return body == null ? R.succ() : R.succ(body);
  }

  protected boolean isVoid(MethodParameter returnType) {
    return Optional.ofNullable(returnType.getMethod())
        .map(Method::getReturnType)
        .filter(t -> t == void.class)
        .isPresent();
  }
}
