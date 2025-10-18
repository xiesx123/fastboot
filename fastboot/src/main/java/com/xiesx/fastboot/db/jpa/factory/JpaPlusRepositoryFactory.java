package com.xiesx.fastboot.db.jpa.factory;

import com.xiesx.fastboot.db.jpa.JpaPlusRepositoryExecutor;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.lang.NonNull;

import javax.persistence.EntityManager;

public class JpaPlusRepositoryFactory extends JpaRepositoryFactory {

    public JpaPlusRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected @NonNull Class<?> getRepositoryBaseClass(@NonNull RepositoryMetadata metadata) {
        return JpaPlusRepositoryExecutor.class;
    }

    @Override
    protected @NonNull JpaRepositoryImplementation<?, ?> getTargetRepository(
            @NonNull RepositoryInformation information, @NonNull EntityManager entityManager) {
        return new JpaPlusRepositoryExecutor<>(information.getDomainType(), entityManager);
    }
}
