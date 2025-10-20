package com.xiesx.fastboot.core.logger.storage;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Log4j2
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LogStorageProvider implements LogStorage {

  private static final String LOG_STORAHE_FORMAT = "| {} | {} | {} | {} | {} | {} | {} | {} ";

  public static final String[] HEAD_IP = {
    "HTTP_X_FORWARDED_FOR",
    "HTTP_X_FORWARDED",
    "HTTP_X_CLUSTER_CLIENT_IP",
    "HTTP_CLIENT_IP",
    "HTTP_FORWARDED_FOR",
    "HTTP_FORWARDED",
    "HTTP_VIA",
    "REMOTE_ADDR",
    "PROXY_FORWARDED_FOR"
  };

  @NonNull public String operation;

  @NonNull public String method;

  @NonNull public Object[] args;

  @NonNull public Long time;

  // =============

  public HttpServletRequest request;

  public Long id;

  public String type;

  public String url;

  public String uri;

  public String ip;

  public Map<String, String> parameters;

  @Generated
  @Override
  public Long record(Object result) {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes == null) {
      log.warn("request attributes is null, maybe not in http request scope.");
      return -1L;
    }
    request = ((ServletRequestAttributes) requestAttributes).getRequest();
    type = request.getMethod();
    url = request.getRequestURL().toString();
    uri = request.getRequestURI();
    ip = getClientIP(request, HEAD_IP);
    parameters = getParamMap(request);
    log.trace(LOG_STORAHE_FORMAT, url, uri, type, method, args, parameters, result, operation);
    return id;
  }

  public static String getClientIP(HttpServletRequest request, String... otherHeaderNames) {
    String[] headers = {
      "X-Forwarded-For",
      "X-Real-IP",
      "Proxy-Client-IP",
      "WL-Proxy-Client-IP",
      "HTTP_CLIENT_IP",
      "HTTP_X_FORWARDED_FOR"
    };
    return getClientIPByHeader(request, ArrayUtil.addAll(headers, otherHeaderNames));
  }

  public static String getClientIPByHeader(HttpServletRequest request, String... headerNames) {
    return Arrays.stream(headerNames)
        // 获取 header 值
        .map(request::getHeader)
        // 过滤掉 null "unknown"
        .filter(ip -> !NetUtil.isUnknown(ip))
        // 找到第一个有效
        .findFirst()
        // 处理代理
        .map(NetUtil::getMultistageReverseProxyIp)
        // fallback
        .orElseGet(() -> NetUtil.getMultistageReverseProxyIp(request.getRemoteAddr()));
  }

  public static Map<String, String> getParamMap(ServletRequest request) {
    final Map<String, String[]> map = request.getParameterMap();
    Map<String, String> params = new HashMap<>();
    for (Map.Entry<String, String[]> entry : Collections.unmodifiableMap(map).entrySet()) {
      params.put(entry.getKey(), ArrayUtil.join(entry.getValue(), StrUtil.COMMA));
    }
    return params;
  }
}
