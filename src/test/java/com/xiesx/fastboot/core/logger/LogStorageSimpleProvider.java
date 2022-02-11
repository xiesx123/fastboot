package com.xiesx.fastboot.core.logger;

import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.app.log.LogRecord;
import com.xiesx.fastboot.app.log.LogRecordRepository;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.core.logger.storage.LogStorageProvider;

import lombok.extern.log4j.Log4j2;

/**
 * @title LogStorageMysqlProvider.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:34
 */
@Log4j2
public class LogStorageSimpleProvider extends LogStorageProvider {

    LogRecordRepository mLogRecordRepository;

    public LogStorageSimpleProvider(String operation, String method, Object[] args, Long time) {
        super(operation, method, args, time);
        mLogRecordRepository = SpringHelper.getBean(LogRecordRepository.class);
    }

    @Override
    public void record(Object result) {
        super.record(result);
        // 构造日志
        LogRecord logRecord = new LogRecord()//
                .setIp(ip)//
                .setMethod(method)//
                .setType(type)//
                .setUrl(uri)//
                .setReq(R.toJsonStr(parameters))//
                .setRes(R.toJsonStr(result))//
                .setTime(time);
        // 保存日志
        logRecord = mLogRecordRepository.insertOrUpdate(logRecord);
        // 打印日志编号
        log.info("log record id {}", logRecord.getId());
    }
}
