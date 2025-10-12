package com.xiesx.fastboot.core.advice;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.AntPathMatcher;

import com.xiesx.fastboot.base.IStatus;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.core.advice.annotation.RestBodyIgnore;
import com.xiesx.fastboot.core.advice.configuration.AdviceProperties;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Map;

@Log4j2
@EnableConfigurationProperties(AdviceProperties.class)
@RestControllerAdvice
public class GlobalBodyAdvice implements ResponseBodyAdvice<Object> {

    @Autowired AdviceProperties properties;

    AntPathMatcher match = new AntPathMatcher();

    public boolean supports(
            MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 获取当前处理请求方法
        Method method = returnType.getMethod();
        if (method == null) {
            return false;
        }
        // 获取类或方法注解
        boolean isSupport =
                AnnotationUtil.hasAnnotation(method.getDeclaringClass(), RestBodyIgnore.class);
        if (!isSupport) {
            isSupport = AnnotationUtil.hasAnnotation(method, RestBodyIgnore.class);
        }
        log.trace("{} body write support {} ", method.getName(), !isSupport);
        // true 表示拦截，false 表示忽略
        return !isSupport;
    }

    @Override
    public Object beforeBodyWrite(
            @Nullable Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        // 是否需要拦截
        boolean isMatch =
                properties.getBodyIgnoresUrls().stream()
                        .anyMatch(i -> match.match(i, request.getURI().getPath()));
        if (isMatch) {
            return body;
        }
        // 获取当前处理请求方法
        Method method = returnType.getMethod();
        if (method != null) {
            log.trace("{} body write advice", method.getName());
            // 处理不同的返回类型
            Class<?> returnTypeCls = method.getReturnType();
            if (returnTypeCls.equals(Void.TYPE)) {
                return null;
            }
        }
        if (body instanceof Map
                || body instanceof Iterable
                || body instanceof String
                || body instanceof IStatus) {
            return body;
        }
        return body == null ? R.succ() : R.succ(body);
    }
}
