package com.xiesx.fastboot.core.logger.storage;

import java.util.Map;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.hutool.extra.servlet.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    private static final String[] HEAD_IP =
            {"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR", "PROXY_FORWARDED_FOR"};

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

    public String ip;

    public Map<String, String> parameters;

    @Override
    public void record(Object result) {
        // 获取请求信息
        request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        type = request.getMethod();
        url = request.getRequestURL().toString();
        uri = request.getRequestURI();
        ip = ServletUtil.getClientIP((javax.servlet.http.HttpServletRequest) request, HEAD_IP);
        parameters = ServletUtil.getParamMap((javax.servlet.ServletRequest) request);
        log.trace(LOG_STORAHE_FORMAT, url, uri, type, method, args, parameters, result, operation);
    }
}
