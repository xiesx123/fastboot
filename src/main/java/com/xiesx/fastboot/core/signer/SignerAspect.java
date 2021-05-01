package com.xiesx.fastboot.core.signer;

import java.lang.reflect.Method;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.base.config.Ordered;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.core.signer.annotation.GoSigner;
import com.xiesx.fastboot.core.signer.cfg.SignerProperties;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
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
    private SignerProperties properties;

    @Pointcut("@annotation(com.xiesx.fastboot.core.signer.annotation.GoSigner)")
    public void signerPointcut() {}

    @Before("signerPointcut()")
    public void before(JoinPoint joinPoint) {
        log.debug("signer pointcut before");
    }

    @After("signerPointcut()")
    public void after() {
        log.debug(" signer pointcut after");
    }

    @Around("signerPointcut()")
    public Object signerBeforeAspect(ProceedingJoinPoint pjp) throws RunException, Throwable {
        // 获取配置
        String header = properties.getHeader();
        String secret = properties.getSecret();
        // 获取方法信息
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        // 获取注解信息
        GoSigner signer = method.getAnnotation(GoSigner.class);
        // 获取请求信息
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 获取参数
        Map<String, String> parms = Maps.newConcurrentMap();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            parms.put(name, value);
        }
        // 是否进行效验
        if (!signer.ignore() && !parms.isEmpty()) {
            // 从header中获取sign
            String headerSign = request.getHeader(header);
            // sign为空
            if (StrUtil.isBlank(headerSign)) {
                throw new RunException(RunExc.SIGN, "非法请求");
            }
            // sign错误
            if (!getSignature(parms, secret).equals(headerSign)) {
                throw new RunException(RunExc.SIGN, "验签失败");
            }
        }
        return pjp.proceed();
    }

    /**
     * 获取签名
     *
     * @param params
     * @param key
     * @return
     */
    public static String getSignature(Map<String, String> params, String key) {
        return SecureUtil.md5(getSortParams(params) + "&key=" + key);
    }

    /**
     * 按key进行正序排列，之间以&相连 <功能描述>
     *
     * @param params
     * @return
     */
    public static String getSortParams(Map<String, String> params) {
        TreeMap<String, String> map = MapUtil.sort(params);
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        String str = "";
        while (iter.hasNext()) {
            String key = iter.next();
            String value = map.get(key);
            str += key + "=" + value + "&";
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
