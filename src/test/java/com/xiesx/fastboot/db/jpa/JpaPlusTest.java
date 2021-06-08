package com.xiesx.fastboot.db.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.app.log.LogRecordRepository;

/**
 * @title JpaPlusTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:01
 */
public class JpaPlusTest {

    @Autowired
    JPAQueryFactory mJpaQuery;

    @Autowired
    LogRecordRepository mLogRecordRepository;

    List<Long> ids = Lists.newArrayList();
}
