package com.xiesx.fastboot.db.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAInsertClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;

/**
 * @title JpaPlusRepository.java
 * @description {@link querydsl}
 *              http://www.querydsl.com/static/querydsl/latest/reference/html/index.html
 * @author xiesx
 * @date 2021-04-04 18:04:00
 */
@NoRepositoryBean
public interface JpaPlusRepository<T, ID> extends JpaRepositoryImplementation<T, ID>, QuerydslPredicateExecutor<T> {

    T findOne(ID id);

    @Override
    List<T> findAll(Predicate predicate);

    @Override
    List<T> findAll(Predicate predicate, Sort sort);

    Page<T> findAll(JPAQuery<T> query, Pageable pageable);

    <S extends T> S insertOrUpdate(S entity);

    <S extends T> List<S> insertOrUpdate(@SuppressWarnings("unchecked") S... entity);

    <S extends T> List<S> insertOrUpdate(List<S> entities);

    int insertOrUpdateRow(@SuppressWarnings("unchecked") T... entities);

    int insertOrUpdateRow(List<T> entities);

    @Deprecated
    int insert(JPAInsertClause insert);

    @Deprecated
    int insert(JPAInsertClause insert, Path<T> path, T entity);

    int update(JPAUpdateClause update);

    int update(JPAUpdateClause update, Predicate... predicate);

    int update(JPAUpdateClause update, Path<T> path, T entity, Predicate... predicate);

    int delete(@SuppressWarnings("unchecked") ID... ids);

    int delete(List<ID> ids);

    int delete(JPADeleteClause delete);

    int delete(JPADeleteClause delete, Predicate... predicate);
}
