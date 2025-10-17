package com.xiesx.fastboot.db.jpa.annotation;

import com.xiesx.fastboot.db.jpa.cfg.JpaPlusCfg;
import com.xiesx.fastboot.db.jpa.factory.JpaPlusRepositoryFactoryBean;

import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.*;

@EnableJpaRepositories(repositoryFactoryBeanClass = JpaPlusRepositoryFactoryBean.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({JpaPlusCfg.class})
@Documented
public @interface EnableJpaPlusRepositories {}
