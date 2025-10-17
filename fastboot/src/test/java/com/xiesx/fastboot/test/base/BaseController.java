package com.xiesx.fastboot.test.base;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.test.LogRecordRepository;
import com.xiesx.fastboot.test.QLogRecord;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    public @Autowired JPAQueryFactory mJPAQueryFactory;

    public @Autowired LogRecordRepository mLogRecordRepository;

    public QLogRecord ql = QLogRecord.logRecord;
}
