package com.xiesx.fastboot.db.jpa;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.*;

/**
 * @title JpaPlusRepositoryExecutor.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:04:05
 */
@SuppressWarnings({"all", "unchecked"})
@Transactional(readOnly = true)
public class JpaPlusRepositoryExecutor<T, ID> extends SimpleJpaRepository<T, ID> implements JpaPlusRepository<T, ID> {

    protected final EntityManager entityManager;

    protected final JPAQueryFactory jpaQueryFactory;

    private final EntityPath<T> path;

    protected final Querydsl querydsl;

    protected final QuerydslJpaPredicateExecutor<T> jpaPredicateExecutor;

    public JpaPlusRepositoryExecutor(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(domainClass);
        this.querydsl = new Querydsl(entityManager, new PathBuilder<T>(path.getType(), path.getMetadata()));
        this.jpaPredicateExecutor =
                new QuerydslJpaPredicateExecutor<>(JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager), entityManager, SimpleEntityPathResolver.INSTANCE, getRepositoryMethodMetadata());
    }

    // ========================== QuerydslPredicateExecutor

    @Override
    public Optional<T> findOne(Predicate predicate) {
        return jpaPredicateExecutor.findOne(predicate);
    }

    @Override
    public List<T> findAll(Predicate predicate) {
        return jpaPredicateExecutor.findAll(predicate);
    }

    @Override
    public List<T> findAll(Predicate predicate, Sort sort) {
        return jpaPredicateExecutor.findAll(predicate, sort);
    }

    @Override
    public List<T> findAll(OrderSpecifier<?>... orders) {
        return jpaPredicateExecutor.findAll(orders);
    }

    @Override
    public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
        return jpaPredicateExecutor.findAll(predicate, orders);
    }

    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        return jpaPredicateExecutor.findAll(predicate, pageable);
    }

    public <S> Page<S> findAll(JPAQuery<S> query, Pageable pageable, OrderSpecifier<?>... orders) {
        // 分页查询
        JPQLQuery<S> jpqlQuery = querydsl.applyPagination(pageable, query).orderBy(orders);
        // 构造分页
        return PageableExecutionUtils.getPage(jpqlQuery.fetch(), pageable, query::fetchCount);
    }

    @Override
    public long count(Predicate predicate) {
        return jpaPredicateExecutor.count(predicate);
    }

    @Override
    public boolean exists(Predicate predicate) {
        return jpaPredicateExecutor.exists(predicate);
    }

    // ========================== JpaPlusRepository

    @Override
    public T findOne(ID id) {
        Optional<T> optional = findById(id);
        return (optional.isPresent()) ? optional.get() : null;
    }

    @Override
    public <S> Page<S> findAll(JPAQuery<S> query, Pageable pageable) {
        // 分页查询
        JPQLQuery<S> jpqlQuery = querydsl.applyPagination(pageable, query);
        // 构造分页
        return PageableExecutionUtils.getPage(jpqlQuery.fetch(), pageable, query::fetchCount);
    }

    @Transactional
    @Override
    public <S extends T> S insertOrUpdate(S entity) {
        return saveAndFlush(entity);
    }

    @Transactional
    @Override
    public <S extends T> List<S> insertOrUpdate(S... entities) {
        List<S> list = saveAll(Arrays.asList(entities));
        entityManager.flush();
        return list;
    }

    @Transactional
    @Override
    public <S extends T> List<S> insertOrUpdate(List<S> entities) {
        List<S> list = saveAll(entities);
        entityManager.flush();
        return list;
    }

    @Transactional
    @Override
    public int insertOrUpdateRow(T... entity) {
        return insertOrUpdate(entity).size();
    }

    @Transactional
    @Override
    public int insertOrUpdateRow(List<T> entities) {
        return insertOrUpdate(entities).size();
    }

    @Transactional
    @Override
    public int insert(JPAInsertClause insert) {
        return (int) insert.execute();
    }

    @Transactional
    @Override
    public int insert(JPAInsertClause insert, Path<T> path, T entity) {
        return (int) insert.set(path, entity).execute();
    }

    @Transactional
    @Override
    public int update(JPAUpdateClause update) {
        return (int) update.execute();
    }

    @Transactional
    @Override
    public int update(JPAUpdateClause update, Path<T> path, T entity, Predicate... predicate) {
        return (int) update.set(path, entity).execute();
    }

    @Transactional
    @Override
    public int update(JPAUpdateClause update, Predicate... predicate) {
        return (int) update.where(predicate).execute();
    }

    @Transactional
    @Override
    public int delete(ID... ids) {
        Assert.notNull(ids, "ids must not be null!");
        int rows = 0;
        for (ID id : ids) {
            deleteById(id);
            rows++;
        }
        return rows;
    }

    @Transactional
    @Override
    public int delete(List<ID> ids) {
        Assert.notNull(ids, "ids must not be null!");
        int rows = 0;
        for (ID id : ids) {
            deleteById(id);
            rows++;
        }
        return rows;
    }

    @Transactional
    @Override
    public int delete(JPADeleteClause delete) {
        return (int) delete.execute();
    }

    @Transactional
    @Override
    public int delete(JPADeleteClause delete, Predicate... predicate) {
        return (int) delete.where(predicate).execute();
    }
}
