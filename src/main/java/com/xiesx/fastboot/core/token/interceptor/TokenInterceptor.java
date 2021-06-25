package com.xiesx.fastboot.core.token.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.core.token.JwtHelper;
import com.xiesx.fastboot.core.token.annotation.GoHeader;
import com.xiesx.fastboot.core.token.annotation.GoToken;
import com.xiesx.fastboot.core.token.cfg.TokenCfg;
import com.xiesx.fastboot.core.token.cfg.TokenProperties;

import cn.hutool.core.text.CharSequenceUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;

/**
 * @title TokenInterceptor.java
 * @description
 * @author xiesx
 * @date 2020-7-21 22:37:38
 */
@Log4j2
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    TokenProperties mTokenProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("token pre handle");
        // 获取token配置
        String key = mTokenProperties.getHeader();
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
                        if (CharSequenceUtil.isBlank(token)) {
                            throw new RunException(RunExc.TOKEN, "未登录");
                        }
                        try {
                            // 解析token
                            Claims claims = JwtHelper.parser(token).getBody();
                            // 设置request
                            request.setAttribute(TokenCfg.UID, claims.getOrDefault(TokenCfg.UID, ""));
                        } catch (Exception e) {
                            log.error("jwt token error", e);
                            if (e instanceof ExpiredJwtException) {
                                throw new RunException(RunExc.TOKEN, "失效");
                            } else {
                                throw new RunException(RunExc.TOKEN);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("token post handle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.debug("token after completion");
    }
}
