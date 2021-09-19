package com.xiesx.fastboot.core.body;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.xiesx.fastboot.base.AbstractStatus;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.core.body.annotation.IgnoreBody;

import cn.hutool.core.annotation.AnnotationUtil;
import lombok.extern.log4j.Log4j2;

/**
 * @title GlobalBodyAdvice.java
 * @description 统一返回
 * @author xiesx
 * @date 2021-04-04 17:52:50
 */
@Log4j2
@RestControllerAdvice
public class GlobalBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        // 获取当前处理请求方法
        Method method = methodParameter.getMethod();
        // 使用注解则忽略
        Boolean isSupport = AnnotationUtil.hasAnnotation(method, IgnoreBody.class);
        log.debug("method ({}) body write ({}) support", method.getName(), isSupport);
        // true 拦截、false 忽略
        return isSupport;
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> converter, ServerHttpRequest req, ServerHttpResponse res) {
        // 获取当前处理请求方法
        Method method = methodParameter.getMethod();
        log.debug("method ({}) before body write advice", method.getName());
        // 获取返回类型
        Class<?> returnType = method.getReturnType();
        // 判断Void类型
        if (returnType.equals(Void.TYPE)) {
            return null;
        } else if (obj instanceof AbstractStatus) {
            return obj;
        } else if (obj instanceof Map<?, ?> || obj instanceof Iterable<?>) {
            return obj;
        } else if (obj instanceof com.alibaba.fastjson.JSON) {
            return obj;
        } else if (obj instanceof String) {
            return obj;
        } else {
            return (obj == null) ? R.succ() : R.succ(obj);
        }
    }
}
