package com.xiesx.fastboot.core.logger.storage;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

import lombok.Data;
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
    public Object result;

    @NonNull
    public Long time;

    // =============

    public String type;

    public String url;

    public String uri;

    public Map<String, String> parameters;

    @Override
    public void record(HttpServletRequest request) {
        // 获取请求信息
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
