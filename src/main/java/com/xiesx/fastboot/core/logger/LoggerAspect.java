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
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
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
        // 获取类信息
        Class<?> cls = pjp.getTarget().getClass();
        // 获取方法信息
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        // 获取类注解
        GoLogger clogger = cls.getAnnotation(GoLogger.class);
        Boolean print = ObjectUtil.isNull(clogger) ? true : clogger.print();
        Boolean format = ObjectUtil.isNull(clogger) ? false : clogger.format();
        String operation = ObjectUtil.isNull(clogger) ? "" : clogger.operation();
        Class<? extends LogStorage> storage = ObjectUtil.isNull(clogger) ? LogStorageProvider.class : clogger.storage();
        // 获取方法注解
        GoLogger mlogger = method.getAnnotation(GoLogger.class);
        if (ObjectUtil.isNotNull(mlogger)) {
            print = mlogger.print();
            format = mlogger.format();
            operation = mlogger.operation();
            storage = mlogger.storage();
        }
        // 获取入参
        Object[] args = pjp.getArgs();
        // 入参过滤
        Object[] argsNew = ArrayUtil.filter(args, new Filter<Object>() {

            @Override
            public boolean accept(Object t) {
                if (t instanceof ServletRequest || t instanceof ServletResponse) { // TODO servlet
                    return false;
                } else if (t instanceof MultipartFile) { // TODO multipart file
                    return false;
                } else if (t instanceof Model) { // model
                    return false;
                } else {
                    return true;
                }
            }
        });
        // 请求参数
        String req = JSON.toJSONString(argsNew, format);
        // 前置打印
        if (print) {
            log.info(LOG_BEFORE_FORMAT, methodName, req);
        }
        // 重新计时
        interval.restart();
        // 方法执行
        Object result = pjp.proceed();
        // 执行时间
        long time = interval.interval();
        // 响应返回
        String json = JSON.toJSONString(result, format);
        // 后置打印
        if (print) {
            log.info(LOG_AFTER_FORMAT, time, methodName, json);
        }
        // 存储实例
        LogStorage logStorage = Singleton.get(storage, operation, methodName, argsNew, time);
        logStorage.record(request, result);
        return result;
    }
}
