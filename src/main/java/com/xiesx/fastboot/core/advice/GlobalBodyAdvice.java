package com.xiesx.fastboot.core.advice;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.xiesx.fastboot.base.AbstractStatus;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.core.advice.annotation.RestBodyIgnore;
import com.xiesx.fastboot.core.advice.configuration.AdviceProperties;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.AntPathMatcher;
import lombok.extern.log4j.Log4j2;

/**
 * @title GlobalBodyAdvice.java
 * @description 统一返回
 * @author xiesx
 * @date 2021-04-04 17:52:50
 */
@Log4j2
@EnableConfigurationProperties(AdviceProperties.class)
@RestControllerAdvice
public class GlobalBodyAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    AdviceProperties properties;

    AntPathMatcher match = new AntPathMatcher();

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        // 获取当前处理请求方法
        Method method = methodParameter.getMethod();
        // 获取类注解
        boolean isSupport = AnnotationUtil.hasAnnotation(method.getDeclaringClass(), RestBodyIgnore.class);
        if (!isSupport) {
            // 获取方法注解
            isSupport = AnnotationUtil.hasAnnotation(method, RestBodyIgnore.class);
        }
        log.trace("{} body write support {} ", method.getName(), !isSupport);
        // true 拦截、false 忽略
        return !isSupport;
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> converter, ServerHttpRequest req, ServerHttpResponse res) {
        // 判断url是否需要拦截
        boolean isAnyMatch = properties.getBodyIgnoresUrls().stream().anyMatch(i -> match.match(i, req.getURI().getPath()));
        if (isAnyMatch) {
            return obj;
        }
        // 获取当前处理请求方法
        Method method = methodParameter.getMethod();
        log.trace("{} body write advice", method.getName());
        // 获取返回类型
        Class<?> returnType = method.getReturnType();
        // 判断Void类型
        if (returnType.equals(Void.TYPE)) {
            return null;
        }
        if (obj instanceof AbstractStatus || obj instanceof String) {
            return obj;
        }
        return obj == null ? R.succ() : R.succ(obj);
    }
}
