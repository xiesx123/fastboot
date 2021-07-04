package com.xiesx.fastboot.db.jpa;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

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

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.log.LogRecord;
import com.xiesx.fastboot.app.log.LogRecordRepository;
import com.xiesx.fastboot.app.log.QLogRecord;

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

    @Test
    @Order(11)
    public void selelct_field() {
        // 默认生成所有属性名查询
        List<LogRecord> list = mLogRecordRepository.findByType("GET");
        // 验证
        assertFalse(list.isEmpty());
    }

    @Test
    @Order(12)
    public void selelct_field_expression() {
        // 内置属性表达式（如：And、Equals.....）
        List<LogRecord> list = mLogRecordRepository.findByTypeAndIp("GET", "127.0.0.1");
        // 验证
        assertFalse(list.isEmpty());
    }

    @Test
    @Order(13)
    public void selelct_query() {
        // 内置注解查询
        List<LogRecord> list = mLogRecordRepository.findByTimeout(10L);
        // 验证
        assertFalse(list.isEmpty());
    }

    @Test
    @Order(14)
    public void selelct_query_dsl() {
        // Querydsl查询
        // 对象
        QLogRecord qLogRecord = QLogRecord.logRecord;
        // 构造查询
        List<LogRecord> list = mJpaQuery.selectFrom(qLogRecord)// 查表
                .where(qLogRecord.type.eq("GET"), // 条件1
                        qLogRecord.ip.eq("127.0.0.1")// 条件2
                )// 条件
                .orderBy(qLogRecord.createDate.desc()) // 排序
                .fetch();
        // 验证
        assertFalse(list.isEmpty());
    }

    @Test
    @Order(21)
    public void page_jpa() {
        // 对象
        QLogRecord q = QLogRecord.logRecord;
        // 条件
        Predicate predicate = q.type.likeIgnoreCase("%GET%");
        // 分页
        Pageable pageable = PageRequest.of(1, 10);
        // 分页
        Page<LogRecord> data = mLogRecordRepository.findAll(predicate, pageable);
        // 验证
        assertFalse(data.getContent().isEmpty());
    }

    @Test
    @Order(22)
    public void page_qdsl() {
        // 对象
        QLogRecord q = QLogRecord.logRecord;
        // 条件
        Predicate predicate = q.type.likeIgnoreCase("%GET%");
        // 分页
        Pageable pageable = PageRequest.of(1, 10);
        // 查询字段
        QBean<LogRecord> fields = Projections.fields(LogRecord.class, q.id, q.method);
        // 构造查询
        JPAQuery<LogRecord> jpaQuery = mJpaQuery.select(fields).from(q).where(predicate);
        // 查询
        Page<LogRecord> data = mLogRecordRepository.findAll(jpaQuery, pageable);
        // 验证
        assertFalse(data.getContent().isEmpty());
    }
}
