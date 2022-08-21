package com.xiesx.fastboot.db.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

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

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @title TestDelete.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:06
 */
@Transactional
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class)
public class TestDelete {

    @Autowired
    JPAQueryFactory mJpaQuery;

    @Autowired
    LogRecordRepository mLogRecordRepository;

    QLogRecord ql = QLogRecord.logRecord;

    List<List<LogRecord>> result = Lists.newArrayList();

    @BeforeEach
    public void befoe() {
        // 零时数据
        List<LogRecord> logRecords = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            logRecords.add(new LogRecord().setIp(StrUtil.format("127.0.{}.1", i)).setMethod("test").setType("GET").setTime(10L));
        }
        // 先删除
        mLogRecordRepository.delete(ql.id.isNotNull());
        // 每份2个，分割5份，方便测试
        result = ListUtil.split(mLogRecordRepository.insertOrUpdate(logRecords), 2);
    }

    @Test
    @Order(1)
    public void delete_jpa() {
        // 按主键逐个删除（逻辑）
        result.get(0).forEach(lr -> {
            mLogRecordRepository.delete(lr.getId());
        });
        // 按对象批量删除（逻辑）
        mLogRecordRepository.deleteAll(result.get(1));
    }

    @Test
    @Order(2)
    public void delete_qdsl() {
        int row = 0;
        // 按主键逐个删除（物理）
        for (LogRecord lr : result.get(0)) {
            row += (int) mJpaQuery.delete(ql).where(ql.id.eq(lr.getId())).execute();
        }
        assertEquals(row, 2);
        // 按对象逐个删除（物理）
        for (LogRecord lr : result.get(1)) {
            row += (int) mJpaQuery.delete(ql).where(ql.eq(lr)).execute();
        }
        assertEquals(row, 4);
        // 按主键批量删除（物理）
        row = (int) mJpaQuery.delete(ql).where(ql.id.in(result.get(2).stream().map(LogRecord::getId).collect(Collectors.toList()))).execute();
        assertEquals(row, 2);
    }

    @Test
    @Order(3)
    public void delete_plus() {
        int row = 0;
        // 按主键逐个删除（逻辑）
        for (LogRecord lr : result.get(0)) {
            row += mLogRecordRepository.delete(lr.getId());
        }
        assertEquals(row, 2);
        // 按主键批量删除（逻辑）
        row = mLogRecordRepository.delete(result.get(1).stream().map(LogRecord::getId).collect(Collectors.toList()));
        assertEquals(row, 2);
        // 按条件批量删除（物理）
        row = mLogRecordRepository.delete(ql.type.eq("GET"));
        assertEquals(row, 10);
    }
}
