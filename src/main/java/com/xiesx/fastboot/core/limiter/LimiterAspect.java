package com.xiesx.fastboot.core.limiter;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;

import com.google.common.util.concurrent.RateLimiter;
import com.xiesx.fastboot.base.config.Ordered;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.core.limiter.annotation.GoLimiter;

import lombok.extern.log4j.Log4j2;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Log4j2
@Component
@Aspect
@Order(Ordered.ASPECT_ORDER_LIMITER)
public class LimiterAspect {

    private RateLimiter rateLimiter = RateLimiter.create(Integer.MAX_VALUE);

    @Pointcut("@annotation(com.xiesx.fastboot.core.limiter.annotation.GoLimiter)")
    public void limiterPointcut() {}

    @Before("limiterPointcut()")
    public void before(JoinPoint joinPoint) {
        log.trace("limiter pointcut before");
    }

    @After("limiterPointcut()")
    public void after() {
        log.trace(" limiter pointcut after");
    }

    @Around("limiterPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取方法
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 获取注解
        GoLimiter limiter = AnnotationUtil.getAnnotation(method, GoLimiter.class);
        // 设置限制
        rateLimiter.setRate(limiter.limit());
        // 尝试能否在timeout时间内获取许可
        if (rateLimiter.tryAcquire()) {
            return point.proceed();
        }
        if (StrUtil.isNotBlank(limiter.message())) {
            throw new RunException(RunExc.LIMITER, limiter.message());
        }
        throw new RunException(RunExc.LIMITER);
    }
}
