package com.xiesx.fastboot.app.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.controller.log.LogRecordRepository;
import com.xiesx.fastboot.controller.user.UserRepository;
import com.xiesx.fastboot.controller.user.UserService;

/**
 * @title BaseController
 * @description
 * @author xiesx
 * @date 2020-4-2410:01:01
 */
public class BaseController {

    @Autowired
    public JPAQueryFactory mJPAQueryFactory;

    @Autowired
    public LogRecordRepository mLogRecordRepository;

    @Autowired
    public UserRepository mSimpleRepository;

    @Autowired
    public UserService mSimpleService;
}
