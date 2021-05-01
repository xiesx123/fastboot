package com.xiesx.fastboot.controller.user;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.xiesx.fastboot.db.jpa.JpaPlusRepository;

/**
 * @title SimpleRepository.java
 * @description
 * @author xiesx
 * @date 2021-04-03 17:45:30
 */
public interface UserRepository extends JpaPlusRepository<User, Long> {

    // 方式1: 默认生成所有属性名查询 （Simple.java -> type属性）
    User findByType(String type);

    // 方式2: 内置属性表达式（如：And、Equals.....）
    User findByUsernameEquals(String username);

    User findByUsernameAndPassword(String username, String password);

    // 方式3: 内置注解查询
    @Query(value = "select * from xx_simple where type=?1", nativeQuery = true)
    List<User> findByStatus(String status);

    @Transactional
    @Modifying
    @Query(value = "update xx_simple set type=?1 , id=?2 ", nativeQuery = true)
    int updateType(Integer type, Long id);
}
