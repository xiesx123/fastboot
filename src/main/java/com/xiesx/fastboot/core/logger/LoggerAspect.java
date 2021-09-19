package com.xiesx.fastboot.core.logger;

import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.xiesx.fastboot.base.config.Ordered;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;
import com.xiesx.fastboot.core.logger.storage.LogStorage;
import com.xiesx.fastboot.core.logger.storage.LogStorageProvider;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.date.TimeInterval;
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
        // 获取类信息
        Class<?> cls = pjp.getTarget().getClass();
        // 获取方法信息
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        // 默认值
        Boolean print = true;
        Boolean format = false;
        String operation = "";
        Class<? extends LogStorage> storage = LogStorageProvider.class;
        // 获取类注解（优先）
        if (AnnotationUtil.hasAnnotation(cls, GoLogger.class)) {
            GoLogger logger = cls.getAnnotation(GoLogger.class);
            print = logger.print();
            format = logger.format();
            operation = logger.operation();
            storage = logger.storage();
        }
        // 获取方法注解
        if (AnnotationUtil.hasAnnotation(method, GoLogger.class)) {
            GoLogger logger = method.getAnnotation(GoLogger.class);
            print = logger.print();
            format = logger.format();
            operation = logger.operation();
            storage = logger.storage();
        }
        // 获取入参
        Object[] args = pjp.getArgs();
        // 入参过滤
        Object[] newArgs = ArrayUtil.filter(args, t -> {
            if (t instanceof ServletRequest || t instanceof ServletResponse || t instanceof MultipartFile || t instanceof Model) {
                return false;
            }
            return true;
        });
        // 请求参数格式化
        String req = JSON.toJSONString(newArgs, format);
        if (print) {
            log.info(LOG_BEFORE_FORMAT, methodName, req);
        }
        // 重新计时
        interval.restart();
        // 方法执行
        Object result = pjp.proceed();
        // 执行时间
        long time = interval.interval();
        // 响应返回格式化
        String res = JSON.toJSONString(result, format);
        if (print) {
            log.info(LOG_AFTER_FORMAT, time, methodName, res);
        }
        // 存储实例
        LogStorage logStorage = Singleton.get(storage, operation, methodName, newArgs, time);
        logStorage.record(result);
        return result;
    }
}
