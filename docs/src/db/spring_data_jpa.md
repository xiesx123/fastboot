# Spring Data Jpa

Spring Data Jpa 的主要类

- 7 个 Repository 接口：

  - 1.Repository
  - 2.CrudRepository
  - 3.PagingAndSortingRepository
  - 4.QueryByExampleExecutor
  - 5.JpaRepository
  - 6.JpaSpeccificationExecutor
  - 7.QueryDslPredicateExecutor

- 2 个实现类：
  - 1.SimpleJpaRepository
  - 2.QueryDslJpaRepository

## 依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

## 扩展

扩展了`JpaRepositoryImplementation`、`QuerydslPredicateExecutor` 在保留原方法基础上新增对`QueryDsl`的支持

```java
public interface JpaPlusRepository<T, ID> extends JpaRepositoryImplementation<T, ID>, QuerydslPredicateExecutor<T> {

    T findOne(ID id);

    Page<T> findAll(JPAQuery<T> query, Pageable pageable);

    Page<T> findAll(JPAQuery<T> query, Pageable pageable, OrderSpecifier<?>... orders);

    <S extends T> S insertOrUpdate(S entity);

    <S extends T> List<S> insertOrUpdate(S... entities);

    <S extends T> List<S> insertOrUpdate(List<S> entities);

    int insertOrUpdateRow(T entity);

    int insertOrUpdateRow(T... entities);

    int insertOrUpdateRow(List<T> entities);

    @Deprecated
    int insert(T entity);

    int update(T entity, Predicate... predicate);

    int delete(ID... ids);

    int delete(List<ID> ids);

    int delete(Predicate... predicate);
}
```

## 使用

启用注解 `@EnableJpaPlusRepositories`

```java
@EnableJpaPlusRepositories
@SpringBootApplication
public class GotvApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GotvApplication.class);
        app.run(args);
    }
}
```

## 示例

### 单表分页

```java
@RequestMapping(value = "page")
public PaginationResult page(BaseVo base, PaginationVo page) {
   // 对象
   QSLog qsLog = QSLog.sLog;
   // 条件
   Predicate predicate = qsLog.id.isNotNull();
   if (ObjectUtils.isNotEmpty(base.getKeyword())) {
       Predicate p1 = qsLog.id.like("%" + base.getKeyword() + "%");
       Predicate p2 = qsLog.method.likeIgnoreCase("%" + base.getKeyword() + "%");
       Predicate p3 = qsLog.type.likeIgnoreCase("%" + base.getKeyword() + "%");
       predicate = ExpressionUtils.and(predicate, ExpressionUtils.anyOf(p1, p2, p3));
   }
   // 排序
   Sort sort = Sort.by(Direction.ASC, SLog.FIELDS.createDate);
   // 分页
   Pageable pageable = PageRequest.of(page.getPage(), page.getLimit(), sort);
   // 查询
   Page<SLog> data = mSLogRepository.findAll(predicate, pageable);
   // 构造
   return PaginationHelper.create(data);
}
```

### 多表分页

```java
@RequestMapping(value = "page")
public PaginationResult page(BaseVo base, PaginationVo page) {
    // 对象
    QUser qUser = QUser.user;
    QSRole qRole = QSRole.sRole;
    QUserRole qUserRole = QUserRole.userRole;
    // 条件
    Predicate predicate = qUser.id.isNotNull();
    if (ObjectUtils.isNotEmpty(base.getKeyword())) {
         Predicate p1 = qUser.id.like("%" + base.getKeyword() + "%");
         Predicate p2 = qUser.username.likeIgnoreCase("%" + base.getKeyword() + "%");
         Predicate p3 = qUser.nickname.likeIgnoreCase("%" + base.getKeyword() + "%");
         predicate = ExpressionUtils.and(predicate, ExpressionUtils.anyOf(p1, p2, p3));
    }
    // 分页
    Pageable pageable = PageRequest.of(page.getPage(), page.getLimit(), Sort.by(Direction.DESC, User.FIELDS.createDate));
    // 查询字段
    QBean<User> selectBean = Projections.fields(User.class
                , qUse // 主表字段
                , qRole.id.longValue().as(User.FIELDS.roleId), //联查字段
                , qRole.name.as(User.FIELDS.roleName));
    // 构造查询
    JPAQuery<User> jpaQuery = mJPAQueryFactory.select(selectBean)
                .from(qUser)
                .leftJoin(qUserRole)
                .on(qUser.id.eq(qUserRole.pk.users))
                .leftJoin(qRole)
                .on(qRole.id.eq(qUserRole.pk.roles))
                .where(predicate);
    // 分页查询
    Page<User> data = mUserRepository.findAll(jpaQuery, pageable);
    // 构造
    return PaginationHelper.create(data);
}
```

### 添加

```java
@RequestMapping(value = "save")
@Transactional
public Result save(BaseVo base) {
   User user = new User();
   int row = mUserRepository.insertOrUpdate(user);
   return (row >= 1) ? R.succ() : R.fail();
}
```

### 修改

```java
@RequestMapping(value = "update")
@Transactional
public Result update(BaseVo base) {
   QUser qUser = QUser.user;
   int row = (int) mJPAQueryFactory.update(qUser).set(qUser.isEnable, 1).where(qUser.id.in(base.getIds()));
   return (row >= 1) ? R.succ() : R.fail();
}
```

### 删除

```java
@RequestMapping(value = "delete")
@Transactional
public Result delete(BaseVo base) {
   int row = mUserRepository.delete(base.getIds());
   return (row >= 1) ? R.succ() : R.fail();
}
```

上述只是部分示例，拓展后常规方法共 48 个
