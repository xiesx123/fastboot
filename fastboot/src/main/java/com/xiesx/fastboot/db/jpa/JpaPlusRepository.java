package com.xiesx.fastboot.db.jpa;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface JpaPlusRepository<T, ID>
    extends JpaRepositoryImplementation<T, ID>, QuerydslPredicateExecutor<T> {

  T findOne(ID id);

  Page<T> findAll(JPAQuery<T> query, Pageable pageable);

  Page<T> findAll(JPAQuery<T> query, Pageable pageable, OrderSpecifier<?>... orders);

  <S extends T> S insertOrUpdate(S entity);

  <S extends T> List<S> insertOrUpdate(S... entities);

  <S extends T> List<S> insertOrUpdate(List<S> entities);

  int insertOrUpdateRow(T entity);

  int insertOrUpdateRow(T... entities);

  int insertOrUpdateRow(List<T> entities);

  int update(T entity, Predicate... predicate);

  int update(Path<T> key, T val, Predicate... predicate);

  <V> int update(Path<V> key, Expression<? extends V> expression, Predicate... predicate);

  int delete(ID... ids);

  int delete(List<ID> ids);

  int delete(Predicate... predicate);
}
