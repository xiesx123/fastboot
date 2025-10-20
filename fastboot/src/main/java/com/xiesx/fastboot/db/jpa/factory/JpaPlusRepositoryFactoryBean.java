package com.xiesx.fastboot.db.jpa.factory;

import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.NonNull;

public class JpaPlusRepositoryFactoryBean<T extends Repository<S, ID>, S, ID>
    extends JpaRepositoryFactoryBean<T, S, ID> {

  public JpaPlusRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  protected @NonNull RepositoryFactorySupport createRepositoryFactory(
      @NonNull EntityManager entityManager) {
    return new JpaPlusRepositoryFactory(entityManager);
  }
}
