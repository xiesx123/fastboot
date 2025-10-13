package com.xiesx.fastboot.db.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.log.LogRecord;
import com.xiesx.fastboot.app.log.LogRecordRepository;
import com.xiesx.fastboot.app.log.QLogRecord;
import com.xiesx.fastboot.db.dsl.QueryDslPojo.LogRecordPojo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class)
public class JpaPlusTest {

    @Autowired JPAQueryFactory mJpaQuery;

    @Autowired LogRecordRepository mLogRecordRepository;

    QLogRecord ql = QLogRecord.logRecord;

    List<LogRecord> result = Lists.newArrayList();

    @BeforeEach
    public void befoe() {
        // 先删除
        mLogRecordRepository.deleteAll();
        // 再添加
        List<LogRecord> logRecords = Lists.newArrayList();
        for (int i = 0; i < 13; i++) {
            logRecords.add(
                    new LogRecord()
                            .setIp(StrUtil.format("127.0.{}.1", i))
                            .setMethod("test")
                            .setType(i % 2 == 0 ? "POST" : "GET")
                            .setTime(i * 10L));
        }
        result = mLogRecordRepository.insertOrUpdate(logRecords);
    }

    @Test
    @Order(1)
    public void select() {
        // 条件
        String keyword = "GET";
        Predicate predicate = ql.id.isNotNull();
        if (ObjectUtil.isNotEmpty(keyword)) {
            Predicate p1 = ql.id.like("%" + keyword + "%");
            Predicate p2 = ql.method.likeIgnoreCase("%" + keyword + "%");
            Predicate p3 = ql.type.likeIgnoreCase("%" + keyword + "%");
            predicate = ExpressionUtils.and(predicate, ExpressionUtils.anyOf(p1, p2, p3));
        }
        // 排序
        Sort sort = Sort.by(Direction.ASC, LogRecord.FIELDS.createDate);
        // 分页
        Pageable pageable = PageRequest.of(0, 10, sort);

        // 分页查询
        Expression<LogRecord> expression = ql;
        JPAQuery<LogRecord> jpaQuery = mJpaQuery.select(expression).from(ql).where(predicate);
        Page<LogRecord> data = mLogRecordRepository.findAll(jpaQuery, pageable);
        assertEquals(data.getContent().size(), 6);

        // 分页查询
        expression = Projections.fields(LogRecord.class, ql, ql.id, ql.ip);
        jpaQuery.select(expression);
        data = mLogRecordRepository.findAll(jpaQuery, pageable);
        assertEquals(data.getContent().size(), 6);

        // 投影查询（在多表联合查询）
        Expression tuple =
                Projections.constructor(
                        LogRecordPojo.class, ql.id, ql.ip, ql.time.min(), ql.time.max());
        jpaQuery.select(tuple);
        assertEquals(jpaQuery.fetch().size(), 1);
    }

    @Test
    @Order(2)
    public void update() {
        // 修改单个
        LogRecord lr = result.get(0);
        lr.setTime(100L);
        LogRecord lr2 = mLogRecordRepository.insertOrUpdate(lr);
        assertEquals(lr2.getTime(), 100);

        // 修改多个
        lr.setTime(101L);
        List<LogRecord> lrs = mLogRecordRepository.insertOrUpdate(Lists.newArrayList(lr));
        assertEquals(lrs.get(0).getTime(), 101);
    }

    @Test
    @Order(3)
    public void delete() {
        // 按对象单个删除
        LogRecord lr = result.get(0);
        mLogRecordRepository.delete(lr);

        // 按条件批量删除
        int row = mLogRecordRepository.delete(ql.type.eq("GET"));
        assertEquals(row, 6);

        // 按主键批量删除
        row = mLogRecordRepository.delete(result.stream().map(LogRecord::getId).toList());
        assertEquals(row, 13);
    }
}
