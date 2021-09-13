package com.xiesx.fastboot.core.logger;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.app.log.LogRecord;
import com.xiesx.fastboot.app.log.LogRecordRepository;
import com.xiesx.fastboot.core.logger.storage.LogStorageProvider;

import lombok.extern.log4j.Log4j2;

/**
 * @title LogStorageMysqlProvider.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:34
 */
@Log4j2
public class LogStorageMysqlProvider extends LogStorageProvider {

    private final static String UNKNOWN = "unknown";

    private static final String[] HEAD_INFO = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR", "PROXY_FORWARDED_FOR", "X-Real-IP"};

    LogRecordRepository mLogRecordRepository;

    public LogStorageMysqlProvider(String operation, String method, Object[] args, Long time) {
        super(operation, method, args, time);
        mLogRecordRepository = SpringHelper.getBean(LogRecordRepository.class);
    }

    @Override
    public void record(Object result) {
        super.record(result);
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

    /**
     * 获取ip
     *
     * @param request
     * @return
     */
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
