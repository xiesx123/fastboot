package com.xiesx.fastboot.db.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.db.dsl.QueryDslPojo.LogRecordPojo;
import com.xiesx.fastboot.test.LogRecord;
import com.xiesx.fastboot.test.LogRecordRepository;
import com.xiesx.fastboot.test.QLogRecord;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class QueryDslTest {

  @Autowired LogRecordRepository mLogRecordRepository;

  @Autowired JPAQueryFactory mJPAQuery;

  QLogRecord ql = QLogRecord.logRecord;

  List<LogRecord> result;

  @BeforeEach
  public void setup() {
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
    List<LogRecord> list = mJPAQuery.selectFrom(ql).orderBy(ql.time.desc()).fetch();
    assertEquals(list.size(), 13);
  }

  @Test
  @Order(2)
  public void where() {
    LogRecord lr =
        mJPAQuery
            .selectFrom(ql)
            .where(ql.method.eq("test"), ql.type.eq("GET"))
            .orderBy(ql.time.desc())
            .fetchFirst();
    assertEquals(lr.getType(), "GET");
  }

  @Test
  @Order(3)
  public void aggregate() {
    List<Tuple> list =
        mJPAQuery.select(ql.type, ql.id.count().as("ct")).from(ql).groupBy(ql.type).fetch();
    assertFalse(list.isEmpty());
    assertEquals(list.get(0).get(1, Long.class), 6);
    assertEquals(list.get(1).get(1, Long.class), 7);
  }

  @Test
  @Order(4)
  public void sub() {
    List<LogRecord> list =
        mJPAQuery
            .selectFrom(ql)
            .where(ql.time.gt(JPAExpressions.select(ql.time.avg()).from(ql)))
            .fetch();
    assertEquals(list.size(), 6);
  }

  @Test
  @Order(5)
  public void tuple() {
    ConstructorExpression<LogRecordPojo> expression =
        Projections.constructor(
            QueryDslPojo.LogRecordPojo.class,
            ql.id,
            ql.ip,
            ql.type,
            ql.time,
            ql.time.min(),
            ql.time.max());
    List<LogRecordPojo> list = mJPAQuery.select(expression).from(ql).groupBy(ql.type).fetch();
    assertEquals(list.size(), 2);
  }

  @Test
  @Order(6)
  public void save() {
    LogRecord lr = result.get(0);
    lr.setTime(100L);
    LogRecord lr2 = mLogRecordRepository.saveAndFlush(lr);
    assertEquals(lr2.getTime(), 100L);
  }

  @Test
  @Order(7)
  public void update() {
    LogRecord lr = result.get(0);
    long row = mJPAQuery.update(ql).set(ql.ip, "localhost").where(ql.id.in(lr.getId())).execute();
    assertEquals(row, 1);
  }

  @Test
  @Order(8)
  public void delete() {
    List<Long> ids = StreamUtil.of(result).map(LogRecord::getId).collect(Collectors.toList());
    long row = mJPAQuery.delete(ql).where(ql.id.in(ids)).execute();
    assertEquals(row, ids.size());
  }
}
