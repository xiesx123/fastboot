package com.xiesx.fastboot.core.token.processor;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.core.token.annotation.GoHeader;
import com.xiesx.fastboot.core.token.annotation.GoToken;
import com.xiesx.fastboot.core.token.cfg.TokenCfg;
import com.xiesx.fastboot.core.token.header.HeaderParam;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @title RequestHeaderMethodProcessor.java
 * @description
 * @author xiesx
 * @date 2020-7-21 22:37:35
 */
public class RequestHeaderMethodProcessor implements HandlerMethodArgumentResolver {

    // public class RequestHeaderMethodProcessor extends RequestResponseBodyMethodProcessor {

    // public RequestHeaderMethodProcessor(List<HttpMessageConverter<?>> converters) {
    // super(converters);
    // }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean h = HeaderParam.class.isAssignableFrom(parameter.getParameterType()) && parameter.hasParameterAnnotation(GoHeader.class);
        boolean s = parameter.getParameterType().isAssignableFrom(String.class) && parameter.hasParameterAnnotation(GoToken.class);
        return h || s;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        // map
        Map<String, Object> map = Maps.newConcurrentMap();
        // 获取用户id
        Object uid = request.getAttribute(TokenCfg.UID, RequestAttributes.SCOPE_REQUEST);
        if (ObjectUtil.isNotNull(uid)) {
            map.put(TokenCfg.UID, uid);
        }
        // 获取其他
        Iterator<String> names = request.getHeaderNames();
        names.forEachRemaining(new Consumer<String>() {

            @Override
            public void accept(String name) {
                map.put(name, request.getHeader(name));
            }
        });
        if (parameter.hasParameterAnnotation(GoToken.class)) {
            return uid.toString();
        } else {
            // 类型
            Class<?> clas = parameter.getParameter().getType();
            // Map转Bean
            return BeanUtil.toBeanIgnoreCase(map, clas, true);
        }
    }
}
