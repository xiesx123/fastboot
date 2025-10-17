package com.xiesx.fastboot.db.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.StrUtil;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.test.LogRecord;
import com.xiesx.fastboot.test.LogRecordRepository;
import com.xiesx.fastboot.test.QLogRecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
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
        Predicate predicate = ql.type.likeIgnoreCase("%GET%");
        // 排序
        Sort sort = Sort.by(Direction.ASC, LogRecord.FIELDS.createDate);
        // 分页
        Pageable pageable = PageRequest.of(0, 10, sort);

        // 分页查询
        Expression<LogRecord> exp = ql;
        JPAQuery<LogRecord> jpaQuery = mJpaQuery.select(exp).from(ql).where(predicate);
        Page<LogRecord> data = mLogRecordRepository.findAll(jpaQuery, pageable);
        assertEquals(data.getContent().size(), 6);

        // 分页查询
        Expression<LogRecord> expFields = Projections.fields(LogRecord.class, ql, ql.id, ql.ip);
        jpaQuery.select(expFields);
        data = mLogRecordRepository.findAll(jpaQuery, pageable);
        assertEquals(data.getContent().size(), 6);

        // 投影查询（在多表联合查询）
        Expression<JpaPlusPojo.LogRecordPojo> expTuple =
                Projections.constructor(
                        JpaPlusPojo.LogRecordPojo.class,
                        ql.id,
                        ql.ip,
                        ql.time.min(),
                        ql.time.max());
        jpaQuery.select(expTuple);
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

        // 按主键批量删除
        int row =
                mLogRecordRepository.delete(
                        StreamUtil.of(result)
                                .skip(1)
                                .map(LogRecord::getId)
                                .collect(Collectors.toList()));
        assertTrue(row > 0);

        // 按条件批量删除
        row = mLogRecordRepository.delete(ql.type.eq("GET"));
        assertTrue(row > 0);
    }
}
