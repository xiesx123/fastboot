package com.xiesx.fastboot.core.logger.storage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.ServletRequest;
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
        ip = getClientIP(request, HEAD_IP);
        parameters = getParamMap(request);
        log.trace(LOG_STORAHE_FORMAT, url, uri, type, method, args, parameters, result, operation);
    }

    public static String getClientIP(HttpServletRequest request, String... otherHeaderNames) {
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
            headers = ArrayUtil.addAll(headers, otherHeaderNames);
        }

        return getClientIPByHeader(request, headers);
    }

    public static String getClientIPByHeader(HttpServletRequest request, String... headerNames) {
        String ip;
        for (String header : headerNames) {
            ip = request.getHeader(header);
            if (false == NetUtil.isUnknown(ip)) {
                return NetUtil.getMultistageReverseProxyIp(ip);
            }
        }

        ip = request.getRemoteAddr();
        return NetUtil.getMultistageReverseProxyIp(ip);
    }

    public static Map<String, String> getParamMap(ServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : getParams(request).entrySet()) {
            params.put(entry.getKey(), ArrayUtil.join(entry.getValue(), StrUtil.COMMA));
        }
        return params;
    }

    public static Map<String, String[]> getParams(ServletRequest request) {
        final Map<String, String[]> map = request.getParameterMap();
        return Collections.unmodifiableMap(map);
    }
}
