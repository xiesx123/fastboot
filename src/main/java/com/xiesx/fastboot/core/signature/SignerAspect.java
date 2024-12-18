package com.xiesx.fastboot.core.signature;

import java.lang.reflect.Method;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xiesx.fastboot.base.config.Ordered;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.core.signature.annotation.GoSigner;
import com.xiesx.fastboot.core.signature.configuration.SignerProperties;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

/**
 * @title SignalAspect.java
 * @description 数据签名切面
 * @author xiesx
 * @date 2020-7-21 22:35:39
 */
@Log4j2
@Component
@Aspect
@Order(Ordered.ASPECT_ORDER_SINGER)
public class SignerAspect {

    @Autowired
    SignerProperties properties;

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
        // 获取方法信息
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        // 获取注解信息
        GoSigner signer = AnnotationUtil.getAnnotation(method, GoSigner.class);
        // 获取请求信息
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 获取参数
        Map<String, String[]> parms = request.getParameterMap();
        // 是否进行效验
        if (!signer.ignore() && !parms.isEmpty()) {
            // 获取sign
            String sign = request.getHeader(key);
            // sign为空
            if (StrUtil.isBlank(sign)) {
                throw new RunException(RunExc.SIGN, "非法请求");
            }
            // sign错误
            if (!SignerHelper.getSignature(parms, secret).equals(sign)) {
                throw new RunException(RunExc.SIGN, "验签失败");
            }
        }
        return pjp.proceed();
    }
}
