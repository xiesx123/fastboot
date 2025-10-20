package com.xiesx.fastboot.db.jpa.annotation;

import com.xiesx.fastboot.db.jpa.cfg.JpaPlusCfg;
import com.xiesx.fastboot.db.jpa.factory.JpaPlusRepositoryFactoryBean;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(repositoryFactoryBeanClass = JpaPlusRepositoryFactoryBean.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({JpaPlusCfg.class})
@Documented
public @interface EnableJpaPlusRepositories {}
