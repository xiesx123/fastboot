package com.xiesx.fastboot.db.jpa.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.xiesx.fastboot.db.jdbc.JdbcTemplatePlus;
import com.xiesx.fastboot.db.jpa.cfg.JpaPlusCfg;
import com.xiesx.fastboot.db.jpa.factory.JpaPlusRepositoryFactoryBean;

/**
 * @title EnableJpaPlusRepositories.java
 * @description
 * @author xiesx
 * @date 2020-7-21 22:32:26
 */
// 启用审计
@EnableJpaAuditing
// 启用JPA
@EnableJpaRepositories(repositoryFactoryBeanClass = JpaPlusRepositoryFactoryBean.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({JpaPlusCfg.class, JdbcTemplatePlus.class})
@Documented
public @interface EnableJpaPlusRepositories {

}
