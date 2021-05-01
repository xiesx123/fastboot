package com.xiesx.fastboot.db.jpa.cfg;

import javax.persistence.EntityManager;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * @title JpaPlusCfg.java
 * @description Jpa
 * @author xiesx
 * @date 2020-7-21 22:36:02
 */
@Configuration
public class JpaPlusCfg {

    /**
     * 默认注入QueryDsl
     * 
     * @param entityManager
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(JPAQueryFactory.class)
    public JPAQueryFactory jpaQuery(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
