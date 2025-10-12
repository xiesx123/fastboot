package com.xiesx.fastboot.core.token.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWTException;

import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.core.token.JwtHelper;
import com.xiesx.fastboot.core.token.annotation.GoHeader;
import com.xiesx.fastboot.core.token.annotation.GoToken;
import com.xiesx.fastboot.core.token.configuration.TokenCfg;
import com.xiesx.fastboot.core.token.configuration.TokenProperties;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;

import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Log4j2
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.trace("token preHandle");
        // 获取token配置
        String key =
                SpringHelper.hasBean(TokenProperties.class).map(TokenProperties::getHeader).get();
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
                            throw new RunException(RunExc.TOKEN, "未认证");
                        }
                        try {
                            // 解析token
                            JSONObject claims = JwtHelper.parser(token).getPayloads();
                            // 是否过期
                            boolean isin =
                                    DateUtil.isIn(
                                            DateUtil.date(),
                                            claims.getDate("iat"),
                                            claims.getDate("exp"));
                            if (!isin) {
                                throw new RunException(RunExc.TOKEN, "已过期");
                            }
                            // 设置uid
                            request.setAttribute(
                                    TokenCfg.UID, claims.getStr(TokenCfg.UID, StrUtil.EMPTY));
                        } catch (Exception e) {
                            log.error("jwt token error", e);
                            String msg = ExceptionUtil.getSimpleMessage(e);
                            if (e instanceof JWTException) {
                                throw new RunException(RunExc.TOKEN, msg);
                            } else if (e instanceof RunException) {
                                RunException run = (RunException) e;
                                throw new RunException(run.getStatus(), run.getMessage());
                            } else {
                                throw new RunException(RunExc.RUNTIME, msg);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable ModelAndView modelAndView)
            throws Exception {
        log.trace("token postHandle");
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable Exception ex)
            throws Exception {
        log.trace("token afterCompletion");
    }
}
