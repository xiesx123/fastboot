package com.xiesx.fastboot.core.logger;

import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.xiesx.fastboot.base.config.Ordered;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;
import com.xiesx.fastboot.core.logger.storage.LogStorage;
import com.xiesx.fastboot.core.logger.storage.LogStorageProvider;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.log4j.Log4j2;

/**
 * @title LoggerAspect.java
 * @description 日志打印切面
 * @author xiesx
 * @date 2020-7-21 22:35:02
 */
@Log4j2
@Component
@Aspect
@Order(Ordered.ASPECT_ORDER_LOGGER)
public class LoggerAspect {

    private static final String LOG_BEFORE_FORMAT = "| request {} | {}";

    private static final String LOG_AFTER_FORMAT = "| response time {}ms | {} | {} ";

    private TimeInterval interval = new TimeInterval();

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void loggerPointcut() {}

    @Before("loggerPointcut()")
    public void before(JoinPoint joinPoint) {
        log.debug("logger pointcut before");
    }

    @After("loggerPointcut()")
    public void after() {
        log.debug(" logger pointcut after");
    }

    @Around("loggerPointcut()")
    public Object loggerAroundAspect(ProceedingJoinPoint pjp) throws Throwable {
        // 获取请求信息
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 获取方法信息
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        // 获取返回类型
        Class<?> returnType = method.getReturnType();
        // 获取注解信息
        GoLogger logger = method.getAnnotation(GoLogger.class);
        Boolean isPrint = Boolean.valueOf(logger == null ? true : logger.print());
        Boolean isFormat = Boolean.valueOf(logger == null ? false : logger.format());
        String operation = Boolean.valueOf(logger == null) ? "" : logger.operation();
        Class<? extends LogStorage> cls = Boolean.valueOf(logger == null) ? LogStorageProvider.class : logger.storage();
        // 获取入参
        Object[] args = pjp.getArgs();
        // 入参过滤
        Object[] argsNew = ArrayUtil.filter(args, new Editor<Object>() {

            @Override
            public Object edit(Object t) {
                if (t instanceof ServletRequest || t instanceof ServletResponse) { // TODO servlet
                    return null;
                } else if (t instanceof MultipartFile) { // TODO multipart file
                    return null;
                } else if (t instanceof Model) { // model
                    return null;
                } else {
                    return t;
                }
            }
        });
        // 请求参数
        String req = JSON.toJSONString(argsNew, isFormat);
        // 前置打印
        if (isPrint) {
            log.info(LOG_BEFORE_FORMAT, methodName, req);
        }
        // 重新计时
        interval.restart();
        // 方法执行
        Object res = "";
        if (!returnType.equals(Void.TYPE)) {
            res = pjp.proceed();
        }
        // 执行时间
        long time = interval.interval();
        // 响应返回
        String result = JSON.toJSONString(res, isFormat);
        // 后置打印
        if (isPrint) {
            log.info(LOG_AFTER_FORMAT, time, methodName, result);
        }
        // 存储实例
        LogStorage storage = Singleton.get(cls, operation, methodName, argsNew, res, time);
        storage.record(request);
        return res;
    }
}
