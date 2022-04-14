package com.xiesx.fastboot.core.body;

import java.lang.reflect.Method;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.xiesx.fastboot.base.AbstractStatus;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.core.body.annotation.RestBodyIgnore;

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

    /**
     * 需要忽略的地址
     */
    private static String[] ignores = new String[] {"/swagger-resources", "/api-docs"};

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
        log.debug("{} body write support {} ", method.getName(), !isSupport);
        // true 拦截、false 忽略
        return !isSupport;
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> converter, ServerHttpRequest req, ServerHttpResponse res) {
        // 判断url是否需要拦截
        if (this.ignoring(req.getURI().toString())) {
            System.out.println(req.getURI().toString());
            return obj;
        }
        // 获取当前处理请求方法
        Method method = methodParameter.getMethod();
        log.debug("{} body write advice", method.getName());
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

    private boolean ignoring(String uri) {
        for (String string : ignores) {
            if (uri.contains(string)) {
                return true;
            }
        }
        return false;
    }
}
