package com.xiesx.fastboot.core.token.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;

import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.core.token.JwtHelper;
import com.xiesx.fastboot.core.token.annotation.GoHeader;
import com.xiesx.fastboot.core.token.annotation.GoToken;
import com.xiesx.fastboot.core.token.configuration.TokenCfg;
import com.xiesx.fastboot.core.token.configuration.TokenProperties;

import lombok.extern.log4j.Log4j2;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler)
            throws Exception {
        log.trace("token preHandle");
        // 获取配置
        String key =
                SpringHelper.hasBean(TokenProperties.class).map(TokenProperties::getHeader).get();
        // 获取秘钥
        String secret =
                Opt.ofNullable(SpringUtil.getProperty("fastboot.token.secret"))
                        .orElse(TokenProperties.SECRET);
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
                            throw new RunException(
                                    RunExc.TOKEN, StrUtil.format("{} is empty", key));
                        }
                        try {
                            // 解析token
                            JSONObject claims = JwtHelper.parser(secret, token).getPayloads();
                            // 是否过期
                            boolean isin =
                                    DateUtil.isIn(
                                            DateUtil.date(),
                                            claims.getDate("iat"),
                                            claims.getDate("exp"));
                            if (!isin) {
                                throw new RunException(RunExc.TOKEN, "token is expired");
                            }
                            // 设置uid
                            request.setAttribute(
                                    TokenCfg.UID, claims.getStr(TokenCfg.UID, StrUtil.EMPTY));
                        } catch (Exception e) {
                            throw new RunException(RunExc.TOKEN, ExceptionUtil.getSimpleMessage(e));
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            @Nullable ModelAndView modelAndView)
            throws Exception {
        log.trace("token postHandle");
    }

    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            @Nullable Exception ex)
            throws Exception {
        log.trace("token afterCompletion");
    }
}
