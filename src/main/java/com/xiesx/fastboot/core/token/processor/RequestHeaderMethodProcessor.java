package com.xiesx.fastboot.core.token.processor;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.core.token.annotation.GoHeader;
import com.xiesx.fastboot.core.token.annotation.GoToken;
import com.xiesx.fastboot.core.token.configuration.TokenCfg;
import com.xiesx.fastboot.core.token.header.HttpHeaderParams;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @title RequestHeaderMethodProcessor.java
 * @description
 * @author xiesx
 * @date 2020-7-21 22:37:35
 */
public class RequestHeaderMethodProcessor implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean h = HttpHeaderParams.class.isAssignableFrom(parameter.getParameterType()) && parameter.hasParameterAnnotation(GoHeader.class);
        boolean s = parameter.getParameterType().isAssignableFrom(String.class) && parameter.hasParameterAnnotation(GoToken.class);
        return h || s;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        // 获取参数
        Map<String, Object> params = Maps.newConcurrentMap();
        request.getHeaderNames().forEachRemaining(k -> {
            params.put(k, request.getHeader(k));
        });
        // 获取用户
        Object uid = request.getAttribute(TokenCfg.UID, RequestAttributes.SCOPE_REQUEST);
        if (ObjectUtil.isNotNull(uid)) {
            params.put(TokenCfg.UID, uid);
        }
        // 返回指定类型
        if (parameter.hasParameterAnnotation(GoToken.class)) {
            return uid.toString();
        }
        return BeanUtil.toBeanIgnoreCase(params, parameter.getParameter().getType(), true);
    }
}
