package com.xiesx.fastboot.core.logger.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xiesx.fastboot.base.config.Configed;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.extern.log4j.Log4j2;

/**
 * @title LogInterceptor.java
 * @description
 * @author xiesx
 * @date 2022-09-24 01:40:06
 */
@Log4j2
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.trace("log preHandle");
        MDC.put(Configed.TRACEID, ObjUtil.defaultIfNull(request.getHeader(Configed.TRACEID), IdUtil.nanoId()));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.trace("log postHandle");
        response.setHeader(Configed.TRACEID, (String) MDC.get(Configed.TRACEID));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.trace("log afterCompletion");
        MDC.remove(Configed.TRACEID);
    }
}
