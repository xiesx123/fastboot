package com.xiesx.fastboot.core.logger.storage;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Maps;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @title LogStorageSimpleProvider.java
 * @description
 * @author xiesx
 * @date 2021-04-17 18:53:36
 */
@Log4j2
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LogStorageProvider implements LogStorage {

    private static final String LOG_STORAHE_FORMAT = "| {} | {} | {} | {} | {} | {} | {} | {} ";

    @NonNull
    public String operation;

    @NonNull
    public String method;

    @NonNull
    public Object[] args;

    @NonNull
    public Long time;

    // =============
    public HttpServletRequest request;

    public String type;

    public String url;

    public String uri;

    public Map<String, String> parameters;

    @Override
    public void record(Object result) {
        // 获取请求信息
        request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        type = request.getMethod();
        url = request.getRequestURL().toString();
        uri = request.getRequestURI();
        // 获取参数
        parameters = Maps.newConcurrentMap();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            parameters.put(name, value);
        }
        log.debug(LOG_STORAHE_FORMAT, url, uri, type, method, args, parameters, result, operation);
    }
}
