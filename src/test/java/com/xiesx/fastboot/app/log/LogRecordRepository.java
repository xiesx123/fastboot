package com.xiesx.fastboot.app.log;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.xiesx.fastboot.db.jpa.JpaPlusRepository;

/**
 * @title LogRecordRepository.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:19:14
 */
public interface LogRecordRepository extends JpaPlusRepository<LogRecord, Long> {

    // 方式1: 默认生成所有属性名查询
    List<LogRecord> findByType(String type);

    // 方式2: 内置属性表达式（如：And、Equals.....）
    List<LogRecord> findByTypeAndIp(String type, String ip);

    // 方式3: 内置注解查询
    @Query(value = "select * from xx_log where time > ?1", nativeQuery = true)
    List<LogRecord> findByTimeout(Long time);

    @Transactional
    @Modifying
    @Query(value = "update xx_log set method=?1, id=?2", nativeQuery = true)
    int updateType(Integer method, Long id);
}
