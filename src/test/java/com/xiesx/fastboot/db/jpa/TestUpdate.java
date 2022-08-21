package com.xiesx.fastboot.db.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.log.LogRecord;
import com.xiesx.fastboot.app.log.LogRecordRepository;
import com.xiesx.fastboot.app.log.QLogRecord;

/**
 * @title TestUpdate.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:06
 */
@Transactional
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class)
public class TestUpdate {

    @Autowired
    JPAQueryFactory mJpaQuery;

    @Autowired
    LogRecordRepository mLogRecordRepository;

    QLogRecord ql = QLogRecord.logRecord;

    LogRecord logRecord;

    @BeforeEach
    public void befoe() {
        logRecord = new LogRecord()//
                .setIp("127.0.0.1")//
                .setMethod("test")//
                .setType("GET");
        logRecord = mLogRecordRepository.insertOrUpdate(logRecord);
    }

    @Test
    @Order(1)
    public void update_jpa() {
        // 修改单个
        logRecord.setTime(1L);
        LogRecord lr = mLogRecordRepository.saveAndFlush(logRecord);
        assertEquals(logRecord.getTime(), 1L);

        // 修改多个
        lr.setTime(2L);
        List<LogRecord> lrs = mLogRecordRepository.saveAllAndFlush(Lists.newArrayList(lr));
        assertEquals(lrs.get(0).getTime(), 2L);
    }

    @Test
    @Order(2)
    public void update_qdsl() {
        // 方式1（key-value）
        int row = (int) mJpaQuery.update(ql).set(ql.time, 3L).where(ql.ip.eq("127.0.0.1")).execute();
        assertEquals(row, 1);

        // 方式2（obj）
        logRecord.setTime(4L);
        row = (int) mJpaQuery.update(ql).set(ql, logRecord).where(ql.ip.eq("127.0.0.1")).execute();
        // 验证
        assertEquals(row, 1);
    }

    @Test
    @Order(3)
    public void update_plus() {
        // 修改单个
        logRecord.setTime(5L);
        LogRecord lr = mLogRecordRepository.insertOrUpdate(logRecord);
        assertEquals(logRecord.getTime(), 5L);

        lr.setTime(6L);
        int row = mLogRecordRepository.insertOrUpdateRow(lr);
        assertEquals(row, 1);

        // 修改多个
        lr.setTime(7L);
        List<LogRecord> lrs = mLogRecordRepository.insertOrUpdate(Lists.newArrayList(lr));
        assertEquals(lrs.get(0).getTime(), 7L);

        lrs.get(0).setTime(8L);
        row = mLogRecordRepository.insertOrUpdateRow(lrs);
        assertEquals(row, 1);

        // 修改多个
        logRecord.setTime(9L);
        row = mLogRecordRepository.update(logRecord, ql.ip.eq("127.0.0.1"));
        assertEquals(row, 1);
    }
}
