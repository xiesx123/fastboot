package com.xiesx.fastboot.core.signature;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.base.config.Configed.Ordered;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.core.signature.annotation.GoSigner;
import com.xiesx.fastboot.core.signature.configuration.SignerProperties;

import lombok.extern.log4j.Log4j2;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@Component
@Aspect
@Order(Ordered.ASPECT_ORDER_SINGER)
public class SignerAspect {

    @Autowired SignerProperties properties;

    @Pointcut("@annotation(com.xiesx.fastboot.core.signature.annotation.GoSigner)")
    public void signerPointcut() {}

    @Before("signerPointcut()")
    public void before(JoinPoint joinPoint) {
        log.trace("signature pointcut before");
    }

    @After("signerPointcut()")
    public void after() {
        log.trace(" signature pointcut after");
    }

    @Around("signerPointcut()")
    public Object signerBeforeAspect(ProceedingJoinPoint pjp) throws RunException, Throwable {
        // 获取配置
        String key = properties.getHeader();
        String secret = properties.getSecret();
        // 获取方法
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        // 获取注解
        GoSigner signer = AnnotationUtil.getAnnotation(method, GoSigner.class);
        // 获取请求
        Map<String, Object> params = Maps.newConcurrentMap();
        HttpServletRequest request = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            log.warn("request attributes is null, maybe not in http request scope.");
            request = ((ServletRequestAttributes) requestAttributes).getRequest();
            request.getParameterMap()
                    .forEach(
                            (k, v) -> {
                                if (v != null) {
                                    if (v.length == 1) {
                                        params.put(k, v[0]);
                                    } else {
                                        params.put(k, Arrays.asList(v));
                                    }
                                }
                            });
        }
        // 是否进行效验
        if (!signer.ignore() && !params.isEmpty() && request != null) {
            // 获取sign
            String sign = request.getHeader(key);
            // sign为空
            if (StrUtil.isBlank(sign)) {
                throw new RunException(RunExc.SIGN, "非法请求");
            }
            if (!SignerHelper.getSignature(params, secret).equals(sign)) {
                throw new RunException(RunExc.SIGN, "验签失败");
            }
        }
        return pjp.proceed();
    }
}
