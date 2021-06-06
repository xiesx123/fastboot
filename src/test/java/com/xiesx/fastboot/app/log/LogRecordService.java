package com.xiesx.fastboot.app.log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * @title LogRecordService.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:19:19
 */
@Service
public class LogRecordService {

    @Autowired
    JPAQueryFactory mJPAQueryFactory;

    @Autowired
    LogRecordRepository mLogRecordRepository;

    public int updateType(String type, Long id) {
        QLogRecord qLogRecord = QLogRecord.logRecord;
        return (int) mJPAQueryFactory.update(qLogRecord).set(qLogRecord.type, type).where(qLogRecord.id.eq(id)).execute();
    }

    public List<LogRecord> findByType(String type) {
        return mLogRecordRepository.findByType(type);
    }
}
