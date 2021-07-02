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
import com.querydsl.jpa.impl.JPAInsertClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.log.LogRecord;
import com.xiesx.fastboot.app.log.LogRecordRepository;
import com.xiesx.fastboot.app.log.QLogRecord;

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

    LogRecord logRecord;

    @BeforeEach
    public void befoe() {
        logRecord = new LogRecord()//
                .setIp("127.0.0.1")//
                .setMethod("test")//
                .setType("GET");
    }

    @Test
    @Order(1)
    public void insert_jpa() {
        // 添加单个，立即生效
        LogRecord lr = mLogRecordRepository.saveAndFlush(logRecord);
        assertNotNull(lr.getId());
        // 添加单个，延迟生效
        lr = mLogRecordRepository.save(logRecord);
        assertNotNull(lr.getId());
        // 添加多个，延迟生效
        List<LogRecord> lrs = mLogRecordRepository.saveAll(Lists.newArrayList(logRecord, logRecord));
        assertEquals(lrs.size(), 2);
        // 手动刷新
        mLogRecordRepository.flush();
    }

    @Test
    @Order(2)
    public void insert_qdsl() {
        // ====================================================//
        // ====================================================//
        // ====================================================//
        int row = 0;
        // 对象
        QLogRecord q = QLogRecord.logRecord;
        // 插入
        JPAInsertClause c = mJpaQuery.insert(q);
        // 方式1（key... 、value...）
        // row += c.columns(q.ip, q.method, q.type, q.url, q.req, q.res, q.time).values("127.0.0.1", "test",
        // "GET", "/test", "", "", 1L).execute();

        // 方式2（key-value）
        c.set(q.ip, "127.0.0.1")//
                .set(q.method, "test")//
                .set(q.type, "GET")//
                .set(q.url, "/test");//
        // row += c.execute();

        // 方式3（obj）
        // row += c.set(q, logRecord).execute();
        // ====================================================//
        // ====================================================//
        // ====================================================//
        // 验证
        assertEquals(row, 0);
    }

    @Test
    @Order(3)
    public void insert_plus() {
        // 添加单个
        LogRecord lr = mLogRecordRepository.insertOrUpdate(logRecord);
        assertNotNull(lr.getId());
        int row = mLogRecordRepository.insertOrUpdateRow(logRecord);
        assertEquals(row, 1);

        // 添加多个
        List<LogRecord> lrs = mLogRecordRepository.insertOrUpdate(Lists.newArrayList(logRecord, logRecord));
        assertEquals(lrs.size(), 2);
        row = mLogRecordRepository.insertOrUpdateRow(Lists.newArrayList(logRecord, logRecord));
        assertEquals(row, 2);
    }
}
