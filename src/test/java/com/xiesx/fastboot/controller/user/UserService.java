package com.xiesx.fastboot.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * @title SimpleService.java
 * @description 业务处理
 * @author Xiesx
 * @date 2021-02-28
 */
@Service
public class UserService {

    @Autowired
    JPAQueryFactory mJPAQueryFactory;

    @Autowired
    UserRepository mSimpleRepository;

    public int updateType(Integer type, Long id) {
        QUser qUser = QUser.user;
        return (int) mJPAQueryFactory.update(qUser).set(qUser.type, type).where(qUser.id.eq(id)).execute();
    }

    public List<User> findByStatus(String status) {
        return mSimpleRepository.findByStatus(status);
    }
}
