package com.xiesx.fastboot.core.token.processor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.core.token.annotation.GoHeader;
import com.xiesx.fastboot.core.token.annotation.GoToken;
import com.xiesx.fastboot.core.token.configuration.TokenCfg;
import com.xiesx.fastboot.core.token.header.RequestHeaderParams;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

public class RequestHeaderMethodProcessor implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        boolean h =
                RequestHeaderParams.class.isAssignableFrom(parameter.getParameterType())
                        && parameter.hasParameterAnnotation(GoHeader.class);
        boolean s =
                parameter.getParameterType().isAssignableFrom(String.class)
                        && parameter.hasParameterAnnotation(GoToken.class);
        return h || s;
    }

    @Override
    public Object resolveArgument(
            @NonNull MethodParameter parameter,
            @Nullable ModelAndViewContainer container,
            @NonNull NativeWebRequest request,
            @Nullable WebDataBinderFactory factory)
            throws Exception {

        // 获取参数
        Map<String, Object> params = Maps.newConcurrentMap();
        request.getHeaderNames()
                .forEachRemaining(
                        k -> {
                            params.put(k, request.getHeader(k));
                        });
        // 获取uid
        Object uid = request.getAttribute(TokenCfg.UID, RequestAttributes.SCOPE_REQUEST);
        if (ObjectUtil.isNotNull(uid)) {
            params.put(TokenCfg.UID, uid);
        }
        // 处理注解
        if (parameter.hasParameterAnnotation(GoToken.class)) {
            return uid != null ? uid.toString() : null;
        }
        return BeanUtil.toBeanIgnoreCase(params, parameter.getParameterType(), true);
    }
}
