package com.xiesx.fastboot.db.jpa.factory;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * @title JpaPlusRepositoryFactoryBean.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:03:50
 */
public class JpaPlusRepositoryFactoryBean<T extends Repository<S, ID>, S, ID> extends JpaRepositoryFactoryBean<T, S, ID> {

    public JpaPlusRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new JpaPlusRepositoryFactory(entityManager);
    }
}
