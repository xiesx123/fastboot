package com.xiesx.fastboot.core.token.processor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.core.token.annotation.GoHeader;
import com.xiesx.fastboot.core.token.annotation.GoToken;
import com.xiesx.fastboot.core.token.configuration.TokenCfg;
import com.xiesx.fastboot.core.token.header.RequestHeaderParams;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

public class RequestHeaderMethodProcessor implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
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
            MethodParameter parameter,
            @Nullable ModelAndViewContainer container,
            NativeWebRequest request,
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
        } else if (parameter.hasParameterAnnotation(GoHeader.class)) {
            return BeanUtil.toBeanIgnoreCase(params, parameter.getParameterType(), true);
        }
        // 默认情况：无注解，也转换为对应类型（支持继承）
        Class<?> type = parameter.getParameterType();
        if (RequestHeaderParams.class.isAssignableFrom(type)) {
            // 参数类型是 RequestHeaderParams 或其子类
            return BeanUtil.toBeanIgnoreCase(params, type, true);
        }
        // 其他情况不处理
        return null;
    }
}
