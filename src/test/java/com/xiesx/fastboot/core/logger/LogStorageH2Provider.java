package com.xiesx.fastboot.core.logger;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.controller.log.LogRecord;
import com.xiesx.fastboot.controller.log.LogRecordRepository;
import com.xiesx.fastboot.core.logger.storage.LogStorageProvider;

import lombok.extern.log4j.Log4j2;

/**
 * @title LogStorageSimpleProvider.java
 * @description
 * @author xiesx
 * @date 2021-04-17 18:53:36
 */
@Log4j2
public class LogStorageH2Provider extends LogStorageProvider {

    private final static String UNKNOWN = "unknown";

    private static final String[] HEAD_INFO = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR", "PROXY_FORWARDED_FOR", "X-Real-IP"};

    LogRecordRepository mLogRecordRepository;

    public LogStorageH2Provider(String operation, String method, Object[] args, Object result, Long time) {
        super(operation, method, args, result, time);
        mLogRecordRepository = SpringHelper.getBean(LogRecordRepository.class);
    }

    @Override
    public void record(HttpServletRequest request) {
        // super.record(request);
        // 构造日志
        LogRecord logRecord = new LogRecord()//
                .setIp(getIpAddr(request))//
                .setMethod(method)//
                .setType(type)//
                .setUrl(uri)//
                .setReq(JSON.toJSONString(parameters))//
                .setRes(JSON.toJSONString(result))//
                .setTime(time);
        // 保存日志
        logRecord = mLogRecordRepository.insertOrUpdate(logRecord);
        // 打印日志编号
        log.info("log record id {}", logRecord.getId());
    }

    public static String getIpAddr(HttpServletRequest request) {
        for (String header : HEAD_INFO) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                if (ip.contains(",")) {
                    String[] ips = ip.split(",");
                    for (String s : ips) {
                        if (!(UNKNOWN.equalsIgnoreCase(s))) {
                            ip = s;
                            break;
                        }
                    }
                }
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
