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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.log.LogRecord;
import com.xiesx.fastboot.app.log.LogRecordRepository;
import com.xiesx.fastboot.app.log.QLogRecord;

import cn.hutool.core.util.StrUtil;

/**
 * @title TestSelect.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:06
 */
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class)
public class TestSelect {

    @Autowired
    JPAQueryFactory mJpaQuery;

    @Autowired
    LogRecordRepository mLogRecordRepository;

    QLogRecord ql = QLogRecord.logRecord;

    @BeforeEach
    public void befoe() {
        // 零时数据
        List<LogRecord> logRecords = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            logRecords.add(new LogRecord().setIp(StrUtil.format("127.0.{}.1", i)).setMethod("test").setType("GET").setTime(10L));
        }
        // 先删除
        mLogRecordRepository.delete(ql.id.isNotNull());
        // 再添加
        mLogRecordRepository.insertOrUpdate(logRecords);
    }

    @Test
    @Order(11)
    public void select_field() {
        // 默认生成所有属性名查询
        assertEquals(mLogRecordRepository.findByType("GET").size(), 10);
    }

    @Test
    @Order(12)
    public void select_field_expression() {
        // 内置属性表达式（如：And、Equals.....）
        assertEquals(mLogRecordRepository.findByTypeAndIp("GET", "127.0.1.1").size(), 1);
    }

    @Test
    @Order(13)
    public void select_query() {
        // 内置注解查询
        assertEquals(mLogRecordRepository.findByTimeout(10L).size(), 10);
    }

    @Test
    @Order(14)
    public void select_query_dsl() {
        // 构造查询
        List<LogRecord> list = mJpaQuery.selectFrom(ql)// 查表
                .where(ql.type.eq("GET"), ql.ip.eq("127.0.1.1"))// 条件
                .orderBy(ql.createDate.desc()) // 排序
                .fetch();
        // 验证
        assertEquals(list.size(), 1);
    }

    @Test
    @Order(21)
    public void page_jpa() {
        // 分页
        Page<LogRecord> data = mLogRecordRepository.findAll(ql.type.likeIgnoreCase("%GET%"), PageRequest.of(0, 10));
        // 验证
        assertEquals(data.getContent().size(), 10);
    }

    @Test
    @Order(22)
    public void page_qdsl() {
        // 条件
        Predicate predicate = ql.type.likeIgnoreCase("%GET%");
        // 分页
        Pageable pageable = PageRequest.of(0, 10);
        // 查询字段
        QBean<LogRecord> fields = Projections.fields(LogRecord.class, ql);
        // 构造查询
        JPAQuery<LogRecord> jpaQuery = mJpaQuery.select(fields).from(ql).where(predicate);
        // 查询
        Page<LogRecord> data = mLogRecordRepository.findAll(jpaQuery, pageable);
        // 验证
        assertEquals(data.getContent().size(), 10);
    }
}
