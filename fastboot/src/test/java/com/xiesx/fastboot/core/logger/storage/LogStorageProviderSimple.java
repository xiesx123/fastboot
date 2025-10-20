package com.xiesx.fastboot.core.logger.storage;

import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.test.LogRecord;
import com.xiesx.fastboot.test.LogRecordRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogStorageProviderSimple extends LogStorageProvider {

  LogRecordRepository mLogRecordRepository;

  public LogStorageProviderSimple(String operation, String method, Object[] args, Long time) {
    super(operation, method, args, time);
  }

  @Override
  public Long record(Object result) {
    LogRecord logRecord =
        new LogRecord()
            .setIp(ip)
            .setMethod(method)
            .setType(type)
            .setUrl(uri)
            .setReq(R.toJsonStr(parameters))
            .setRes(R.toJsonStr(result))
            .setTime(time);
    log.info(
        "log id {}",
        SpringHelper.getBean(LogRecordRepository.class).insertOrUpdate(logRecord).getId());
    return logRecord.getId();
  }
}
