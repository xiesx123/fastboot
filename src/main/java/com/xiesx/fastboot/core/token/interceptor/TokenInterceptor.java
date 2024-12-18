package com.xiesx.fastboot.core.token.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.core.token.JwtHelper;
import com.xiesx.fastboot.core.token.annotation.GoHeader;
import com.xiesx.fastboot.core.token.annotation.GoToken;
import com.xiesx.fastboot.core.token.configuration.TokenCfg;
import com.xiesx.fastboot.core.token.configuration.TokenProperties;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

/**
 * @title TokenInterceptor.java
 * @description
 * @author xiesx
 * @date 2020-7-21 22:37:38
 */
@Log4j2
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.trace("token preHandle");
        // 获取token配置
        String key = SpringHelper.hasBean(TokenProperties.class).map(TokenProperties::getHeader).get();
        // 获取方法信息
        if (handler instanceof HandlerMethod) {
            // 获取方法
            Method method = ((HandlerMethod) handler).getMethod();
            // 获取参数注解信息
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (Annotation[] annotation1 : parameterAnnotations) {
                for (Annotation annotation2 : annotation1) {
                    if (annotation2 instanceof GoToken || annotation2 instanceof GoHeader) {
                        // 获取token
                        String token = request.getHeader(key);
                        if (StrUtil.isBlank(token)) {
                            throw new RunException(RunExc.TOKEN, "未登录");
                        }
                        try {
                            // 解析token
                            JSONObject claims = JwtHelper.parser(token).getPayloads();
                            // 设置request
                            request.setAttribute(TokenCfg.UID, claims.getStr(TokenCfg.UID, StrUtil.EMPTY));
                        } catch (Exception e) {
                            log.error("jwt token error", e);
                            if (e instanceof ValidateException) {
                                throw new RunException(RunExc.TOKEN, ExceptionUtil.getSimpleMessage(e));
                            }
                            throw new RunException(RunExc.TOKEN);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.trace("token postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.trace("token afterCompletion");
    }
}
