package com.xiesx.fastboot.db.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.test.LogRecord;
import com.xiesx.fastboot.test.LogRecordRepository;
import com.xiesx.fastboot.test.QLogRecord;
import java.util.List;
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

@Transactional
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class JpaPlusTest {

  @Autowired JPAQueryFactory mJpaQuery;

  @Autowired LogRecordRepository mLogRecordRepo;

  QLogRecord ql = QLogRecord.logRecord;

  List<LogRecord> result = Lists.newArrayList();

  @BeforeEach
  public void setup() {
    // 先删除
    mLogRecordRepo.deleteAll();
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
    result = mLogRecordRepo.insertOrUpdate(logRecords);
  }

  @Test
  @Order(1)
  public void select() {
    // 按id
    LogRecord lr = result.get(0);
    LogRecord lr2 = mLogRecordRepo.findOne(lr.getId());
    LogRecord lr3 = mLogRecordRepo.findOne(1L);
    assertNotNull(lr2);
    assertEquals(lr.getId(), lr2.getId());
    assertNull(lr3);

    // 是否存在
    boolean exist = mLogRecordRepo.exists(ql.id.eq(result.get(0).getId()));
    assertTrue(exist);
  }

  @Test
  @Order(2)
  public void page() {
    // 条件
    Predicate predicate = ql.type.likeIgnoreCase("%GET%");
    // 排序
    Sort sort = Sort.by(Direction.ASC, LogRecord.FIELDS.createDate);
    OrderSpecifier<?>[] sorts = new OrderSpecifier<?>[] {ql.createDate.asc(), ql.type.desc()};
    // 分页
    Pageable pageable = PageRequest.of(0, 10, sort);

    // 分页查询
    Expression<LogRecord> exp = ql;
    JPAQuery<LogRecord> jpaQuery = mJpaQuery.select(exp).from(ql).where(predicate);
    Page<LogRecord> data = mLogRecordRepo.findAll(jpaQuery, pageable);
    assertEquals(data.getContent().size(), 6);
    data = mLogRecordRepo.findAll(jpaQuery, pageable, sorts);
    assertEquals(data.getContent().size(), 6);

    // 分页查询
    Expression<LogRecord> expFields = Projections.fields(LogRecord.class, ql, ql.id, ql.ip);
    jpaQuery.select(expFields);
    data = mLogRecordRepo.findAll(jpaQuery, pageable);
    assertEquals(data.getContent().size(), 6);

    // 投影查询（在多表联合查询）
    Expression<JpaPlusTestPojo.LogRecordPojo> expTuple =
        Projections.constructor(
            JpaPlusTestPojo.LogRecordPojo.class, ql.id, ql.ip, ql.time.min(), ql.time.max());
    jpaQuery.select(expTuple);
    assertEquals(jpaQuery.fetch().size(), 1);
  }

  @Test
  @Order(3)
  public void insertOrUpdate() {
    // 单个
    LogRecord lr11 = result.get(0);
    lr11.setTime(100L);
    LogRecord lr12 = mLogRecordRepo.insertOrUpdate(lr11);
    assertEquals(lr12.getTime(), 100);
    lr12.setTime(101L);
    int row = mLogRecordRepo.insertOrUpdateRow(lr12);
    assertEquals(row, 1);

    // 多个
    LogRecord lr21 = result.get(0);
    lr21.setTime(102L);
    LogRecord lr22 = result.get(1);
    lr22.setTime(103L);
    List<LogRecord> lrs3 = mLogRecordRepo.insertOrUpdate(lr21, lr22);
    assertEquals(lrs3.get(0).getTime(), 102);
    assertEquals(lrs3.get(1).getTime(), 103);
    lr21.setTime(104L);
    lr22.setTime(104L);
    row = mLogRecordRepo.insertOrUpdateRow(lr21, lr22);
    assertEquals(row, 2);
  }

  @Test
  @Order(4)
  public void update() {
    LogRecord lr = result.get(0);
    Long id = lr.getId();
    // 条件
    Predicate predicate = ql.id.eq(id);

    lr.setTime(101L);
    int row = mLogRecordRepo.update(lr, predicate);
    assertTrue(row > 0);
    assertEquals(mLogRecordRepo.findOne(id).getTime(), 101);

    lr.setTime(102L);
    row = mLogRecordRepo.update(ql, lr, predicate);
    assertTrue(row > 0);
    assertEquals(mLogRecordRepo.findOne(id).getTime(), 102);

    row = mLogRecordRepo.update(ql.time, Expressions.constant(103L), predicate);
    assertTrue(row > 0);
    assertEquals(mLogRecordRepo.findOne(id).getTime(), 103);
  }

  @Test
  @Order(5)
  public void delete() {
    mLogRecordRepo.delete(result.get(0).getId(), result.get(1).getId());

    int row = mLogRecordRepo.delete(ql.type.eq("GET"));
    assertTrue(row > 0);
  }
}
