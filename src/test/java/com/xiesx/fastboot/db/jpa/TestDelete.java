package com.xiesx.fastboot.db.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.querydsl.jpa.impl.JPADeleteClause;
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
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class)
public class TestDelete {

    @Autowired
    JPAQueryFactory mJpaQuery;

    @Autowired
    LogRecordRepository mLogRecordRepository;

    List<Long> ids = Lists.newArrayList();

    List<List<LogRecord>> result = Lists.newArrayList();

    @BeforeEach
    public void befoe() {
        // 零时存储数据
        List<LogRecord> logRecords = Lists.newArrayList();
        // 构造日志
        for (int i = 1; i <= 10; i++) {
            LogRecord logRecord = new LogRecord()//
                    .setIp(StrUtil.format("127.0.{}.1", i))//
                    .setMethod("test")//
                    .setType("GET")//
                    .setUrl("/test")//
                    .setReq("")//
                    .setRes("")//
                    .setTime(1L);
            logRecords.add(logRecord);
        }
        // 保存日志
        List<LogRecord> list = mLogRecordRepository.insertOrUpdate(logRecords);
        // 每份2个，分割5份，方便测试
        result = ListUtil.split(list, 2);
    }

    @Test
    @Order(1)
    public void delete_jpa() {
        // 按对象删除
        for (LogRecord lr : result.get(0)) {
            mLogRecordRepository.delete(lr);
        }

        // 按主键删除
        for (LogRecord lr : result.get(1)) {
            mLogRecordRepository.deleteById(lr.getId());
        }
    }

    @Test
    @Order(2)
    public void delete_qdsl() {
        int row = 0;
        // 按Q对象删除（物理）
        QLogRecord q = QLogRecord.logRecord;
        for (LogRecord lr : result.get(0)) {
            row += mJpaQuery.delete(q).where(q.id.eq(lr.getId())).execute();
        }
        assertEquals(row, 2);
    }

    @Test
    @Order(3)
    public void delete_plus() {
        int row = 0;
        // 按主键删除（逻辑）
        for (LogRecord lr : result.get(0)) {
            row += mLogRecordRepository.delete(lr.getId());
        }

        // 按List<主键>删除（逻辑）
        for (LogRecord lr : result.get(1)) {
            ids.add(lr.getId());
        }
        row += mLogRecordRepository.delete(ids);

        // 按Q对象删除（物理）
        QLogRecord q = QLogRecord.logRecord;
        for (LogRecord lr : result.get(2)) {
            JPADeleteClause jpaDeleteClause = mJpaQuery.delete(q).where(q.id.eq(lr.getId()));
            row += mLogRecordRepository.delete(jpaDeleteClause);
        }

        // 按Q对象删除（物理）
        for (LogRecord lr : result.get(3)) {
            JPADeleteClause jpaDeleteClause = mJpaQuery.delete(q).where(q.id.eq(lr.getId()));
            row += mLogRecordRepository.delete(jpaDeleteClause, q.type.eq("GET"));
        }
        assertEquals(row, 8);
    }
}
