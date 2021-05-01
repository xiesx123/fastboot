package com.xiesx.fastboot.db.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;

/**
 * @title JpaPlusRepository.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:04:00
 */
@SuppressWarnings({"all", "unchecked"})
@NoRepositoryBean
public interface JpaPlusRepository<T, ID> extends JpaRepositoryImplementation<T, ID>, QuerydslPredicateExecutor<T> {

    T findOne(ID id);

    @Override
    List<T> findAll(Predicate predicate);

    @Override
    List<T> findAll(Predicate predicate, Sort sort);

    <S> Page<S> findAll(JPAQuery<S> query, Pageable pageable);

    <O extends T> O insertOrUpdate(O entitie);

    <O extends T> List<O> insertOrUpdate(O... entities);

    <O extends T> List<O> insertOrUpdate(List<O> entities);

    int insertOrUpdateRow(T... entities);

    int insertOrUpdateRow(List<T> entities);

    int update(JPAUpdateClause update);

    int update(JPAUpdateClause update, Predicate... predicate);

    int delete(ID... ids);

    int delete(List<ID> ids);

    int delete(JPADeleteClause delete);

    int delete(JPADeleteClause delete, Predicate... predicate);
}
