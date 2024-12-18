package com.xiesx.fastboot.core.logger;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.xiesx.fastboot.base.config.Ordered;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;
import com.xiesx.fastboot.core.logger.storage.LogStorage;
import com.xiesx.fastboot.core.logger.storage.LogStorageProvider;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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

    private static final String LOG_BEFORE_FORMAT = "| request  {} | {}";

    private static final String LOG_AFTER_FORMAT = "| response {}ms | {} | {} ";

    private TimeInterval interval = new TimeInterval();

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void loggerPointcut() {}

    @Before("loggerPointcut()")
    public void before(JoinPoint joinPoint) {
        log.trace("logger pointcut before");
    }

    @After("loggerPointcut()")
    public void after() {
        log.trace(" logger pointcut after");
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
        boolean print = true;
        boolean format = false;
        String operation = StrUtil.EMPTY;
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
        // 执行前打印入参
        if (print) {
            log.info(LOG_BEFORE_FORMAT, methodName, format ? R.toJsonPrettyStr(newArgs) : R.toJsonStr(newArgs));
        }
        // 重新计时
        interval.restart();
        // 方法执行
        Object result = pjp.proceed();
        // 执行时间
        long time = interval.interval();
        // 执行后打印结果
        if (print) {
            log.info(LOG_AFTER_FORMAT, time, methodName, format ? R.toJsonPrettyStr(result) : R.toJsonStr(result));
        }
        // 日志存储
        Singleton.get(storage, operation, methodName, newArgs, time).record(result);
        return result;
    }
}
