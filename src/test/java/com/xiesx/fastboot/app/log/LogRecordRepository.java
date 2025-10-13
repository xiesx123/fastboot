package com.xiesx.fastboot.app.log;

import com.xiesx.fastboot.db.jpa.JpaPlusRepository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogRecordRepository extends JpaPlusRepository<LogRecord, Long> {

    // 方式1: 默认生成所有属性名查询
    List<LogRecord> findByType(String type);

    // 方式2: 内置属性表达式（如：And、Equals.....）
    List<LogRecord> findByTypeAndIp(String type, String ip);

    // 方式3: 内置注解查询、事务更新
    @Query(value = "select * from xx_log where time >= ?1", nativeQuery = true)
    List<LogRecord> findByTimeout(Long time);

    // 方式4: 内置QueryDsl
    // List<LogRecord> findAll(Predicate predicate);
}
