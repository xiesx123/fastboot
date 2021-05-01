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

import com.xiesx.fastboot.base.AbstractState;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.core.body.annotation.IgnoreBody;

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
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> converter, ServerHttpRequest req, ServerHttpResponse res) {
        // 获取当前处理请求方法
        Method method = methodParameter.getMethod();
        // 获取方法名
        String methodName = method.getName();
        log.debug("method ({}) before body write advice", methodName);
        // 获取返回类型
        Class<?> returnType = method.getReturnType();
        // 判断Void类型
        if (returnType.equals(Void.TYPE)) {
            return null;
        } else {
            if (obj instanceof AbstractState) {
                return obj;
            } else if (obj instanceof Map<?, ?> || obj instanceof Iterable<?>) {
                return obj;
            } else if (obj instanceof com.alibaba.fastjson.JSON) {
                return obj;
            } else if (obj instanceof String) {
                return obj;
            } else if (obj == null) {
                return R.succ();
            } else {
                return R.succ(obj);
            }
        }
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        // 获取当前处理请求方法
        Method method = methodParameter.getMethod();
        // 获取方法名
        String methodName = method.getName();
        // 获取方法注解
        IgnoreBody annotation = methodParameter.getMethod().getAnnotation(IgnoreBody.class);
        // 使用注解则忽略
        Boolean isSupport = (annotation == null);
        log.debug("method ({}) body write ({}) support", methodName, isSupport);
        // true 拦截、false 忽略
        return isSupport;
    }
}
