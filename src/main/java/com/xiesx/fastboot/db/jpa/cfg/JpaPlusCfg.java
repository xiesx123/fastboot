package com.xiesx.fastboot.db.jpa.cfg;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;


/**
 * @title JpaPlusCfg.java
 * @description Jpa
 * @author xiesx
 * @date 2020-7-21 22:36:02
 */
@Configuration
public class JpaPlusCfg {

    @Bean
    @ConditionalOnMissingBean(JPAQueryFactory.class)
    public JPAQueryFactory jpaQuery(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
