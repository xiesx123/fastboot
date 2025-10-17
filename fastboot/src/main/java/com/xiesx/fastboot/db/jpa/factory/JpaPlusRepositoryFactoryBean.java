package com.xiesx.fastboot.db.jpa.factory;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.NonNull;

import javax.persistence.EntityManager;

public class JpaPlusRepositoryFactoryBean<T extends Repository<S, ID>, S, ID>
        extends JpaRepositoryFactoryBean<T, S, ID> {

    public JpaPlusRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected @NonNull RepositoryFactorySupport createRepositoryFactory(
            EntityManager entityManager) {
        return new JpaPlusRepositoryFactory(entityManager);
    }
}
