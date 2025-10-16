package com.xiesx.fastboot.test.log;

import com.xiesx.fastboot.db.jpa.JpaPlusRepository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogRecordRepository extends JpaPlusRepository<LogRecord, Long> {

    // 方式1: 属性名
    List<LogRecord> findByType(String type);

    // 方式2: 属性表达式（如：And、Equals.....）
    List<LogRecord> findByTypeAndIp(String type, String ip);

    // 方式3: 注解查询、事务更新
    @Query(value = "select * from xx_log where time >= ?1", nativeQuery = true)
    List<LogRecord> findByTimeout(Long time);

    // 方式4: QueryDsl
    // List<LogRecord> findAll(Predicate predicate);
}
