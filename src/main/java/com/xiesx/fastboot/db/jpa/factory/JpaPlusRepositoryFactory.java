package com.xiesx.fastboot.db.jpa.factory;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import com.xiesx.fastboot.db.jpa.JpaPlusRepositoryExecutor;

/**
 * @title JpaPlusRepositoryFactory.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:03:44
 */
public class JpaPlusRepositoryFactory extends JpaRepositoryFactory {

    public JpaPlusRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return JpaPlusRepositoryExecutor.class;
    }

    @Override
    protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
        return new JpaPlusRepositoryExecutor<>(information.getDomainType(), entityManager);
    }
}
