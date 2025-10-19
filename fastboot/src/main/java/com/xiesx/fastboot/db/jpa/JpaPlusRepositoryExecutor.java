package com.xiesx.fastboot.db.jpa;

import cn.hutool.core.lang.Assert;

import com.google.common.collect.Lists;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.persistence.EntityManager;

@Transactional(readOnly = true)
public class JpaPlusRepositoryExecutor<T, ID> extends SimpleJpaRepository<T, ID>
        implements JpaPlusRepository<T, ID> {

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
        this.querydsl =
                new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.jpaPredicateExecutor =
                new QuerydslJpaPredicateExecutor<>(
                        JpaEntityInformationSupport.getEntityInformation(
                                domainClass, entityManager),
                        entityManager,
                        SimpleEntityPathResolver.INSTANCE,
                        getRepositoryMethodMetadata());
    }

    // ========================== QuerydslPredicateExecutor

    @Override
    public @NonNull Optional<T> findOne(@NonNull Predicate predicate) {
        return jpaPredicateExecutor.findOne(predicate);
    }

    @Override
    public @NonNull List<T> findAll(@NonNull Predicate predicate) {
        return jpaPredicateExecutor.findAll(predicate);
    }

    @Override
    public @NonNull List<T> findAll(@NonNull Predicate predicate, @NonNull Sort sort) {
        return jpaPredicateExecutor.findAll(predicate, sort);
    }

    @Override
    public @NonNull List<T> findAll(@NonNull OrderSpecifier<?>... orders) {
        return jpaPredicateExecutor.findAll(orders);
    }

    @Override
    public @NonNull List<T> findAll(
            @NonNull Predicate predicate, @NonNull OrderSpecifier<?>... orders) {
        return jpaPredicateExecutor.findAll(predicate, orders);
    }

    @Override
    public @NonNull Page<T> findAll(@NonNull Predicate predicate, @NonNull Pageable pageable) {
        return jpaPredicateExecutor.findAll(predicate, pageable);
    }

    @Override
    public @NonNull <S extends T, R> R findBy(
            @NonNull Predicate predicate,
            @NonNull Function<FetchableFluentQuery<S>, R> queryFunction) {
        return jpaPredicateExecutor.findBy(predicate, queryFunction);
    }

    @Override
    public long count(@NonNull Predicate predicate) {
        return jpaPredicateExecutor.count(predicate);
    }

    @Override
    public boolean exists(@NonNull Predicate predicate) {
        return jpaPredicateExecutor.exists(predicate);
    }

    // ========================== JpaPlusRepository

    @Override
    public T findOne(ID id) {
        Optional<T> optional = findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Page<T> findAll(JPAQuery<T> query, Pageable pageable) {
        JPQLQuery<T> jpqlQuery = querydsl.applyPagination(pageable, query);
        return PageableExecutionUtils.getPage(jpqlQuery.fetch(), pageable, jpqlQuery::fetchCount);
    }

    @Override
    public Page<T> findAll(JPAQuery<T> query, Pageable pageable, OrderSpecifier<?>... orders) {
        JPQLQuery<T> jpqlQuery = querydsl.applyPagination(pageable, query).orderBy(orders);
        return PageableExecutionUtils.getPage(jpqlQuery.fetch(), pageable, jpqlQuery::fetchCount);
    }

    @Transactional
    @Override
    public <S extends T> S insertOrUpdate(S entity) {
        return saveAndFlush(entity);
    }

    @Transactional
    @Override
    public <S extends T> List<S> insertOrUpdate(S... entities) {
        return insertOrUpdate(Arrays.asList(entities));
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
    public int insertOrUpdateRow(T entity) {
        return insertOrUpdateRow(Lists.newArrayList(entity));
    }

    @Transactional
    @Override
    public int insertOrUpdateRow(T... entities) {
        return insertOrUpdateRow(Arrays.asList(entities));
    }

    @Transactional
    @Override
    public int insertOrUpdateRow(List<T> entities) {
        List<T> list = saveAll(entities);
        entityManager.flush();
        return list.size();
    }

    @Transactional
    @Override
    public int insert(T entity) {
        // return (int) jpaQueryFactory.insert(path).set(path, entity).execute();
        return 0;
    }

    @Transactional
    @Override
    public int update(T entity, Predicate... predicate) {
        return (int) jpaQueryFactory.update(path).set(path, entity).where(predicate).execute();
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
    public int delete(Predicate... predicate) {
        return (int) jpaQueryFactory.delete(path).where(predicate).execute();
    }
}
