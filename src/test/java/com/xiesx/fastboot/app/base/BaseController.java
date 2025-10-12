package com.xiesx.fastboot.app.base;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.app.log.LogRecordRepository;
import com.xiesx.fastboot.app.log.QLogRecord;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    public @Autowired JPAQueryFactory mJPAQueryFactory;

    public @Autowired LogRecordRepository mLogRecordRepository;

    public QLogRecord ql = QLogRecord.logRecord;
}
