package com.xiesx.fastboot.db.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.log.LogRecord;
import com.xiesx.fastboot.app.log.LogRecordRepository;
import com.xiesx.fastboot.app.log.QLogRecord;

import cn.hutool.core.text.CharSequenceUtil;

/**
 * @title TestInsert.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:06
 */
@Transactional
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class)
public class TestInsert {

    @Autowired
    JPAQueryFactory mJpaQuery;

    @Autowired
    LogRecordRepository mLogRecordRepository;

    QLogRecord ql = QLogRecord.logRecord;

    List<LogRecord> logRecords = Lists.newArrayList();

    @BeforeEach
    public void befoe() {
        for (int i = 1; i <= 10; i++) {
            logRecords.add(new LogRecord().setIp(CharSequenceUtil.format("127.0.{}.1", i)).setMethod("test").setType("GET").setTime(10L));
        }
    }

    @Test
    @Order(1)
    public void insert_jpa() {
        // 添加1个
        LogRecord lr = mLogRecordRepository.saveAndFlush(logRecords.get(0));
        assertNotNull(lr.getId());
        // 添加2个
        List<LogRecord> lrs = mLogRecordRepository.saveAllAndFlush(logRecords.subList(1, 3));
        assertEquals(lrs.size(), 2);
    }

    @Test
    @Order(2)
    public void insert_qdsl() {
        // ====================================================//
        // ====================================================//
        // ====================================================//
        int row = 0;
        // 插入
        // JPAInsertClause c = mJpaQuery.insert(ql);
        // 方式1（key... 、value...）
        // row += c.columns(q.ip, q.method, q.type, q.url, q.req, q.res, q.time).values("127.0.0.1", "test",
        // "GET", "/test", "", "", 1L).execute();

        // 方式2（key-value）
        // row += c.set(ql.ip, "127.0.0.1").set(ql.method, "test").set(ql.type, "GET").set(ql.url,
        // "/test").execute();

        // 方式3（obj）
        // row += c.set(q, logRecords.get(0)).execute();
        // ====================================================//
        // ====================================================//
        // ====================================================//
        // 验证
        assertEquals(row, 0);
    }

    @Test
    @Order(3)
    public void insert_plus() {
        int row = 0;
        // 添加单个
        LogRecord lr = mLogRecordRepository.insertOrUpdate(logRecords.get(0));
        assertNotNull(lr.getId());
        row = mLogRecordRepository.insertOrUpdateRow(logRecords.get(1));
        assertEquals(row, 1);

        // 添加多个
        List<LogRecord> lrs = mLogRecordRepository.insertOrUpdate(logRecords.subList(2, 4));
        assertEquals(lrs.size(), 2);
        row = mLogRecordRepository.insertOrUpdateRow(logRecords.subList(4, 6));
        assertEquals(row, 2);
    }
}
