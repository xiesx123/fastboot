# Spring Data Jpa

基于 **JPA** 标准，并提供了一套简洁的 API 和注解，封装 JPA 的复杂性，简化了数据层的开发工作，使开发人员能够更专注于业务逻辑的实现

```java
public interface LogRecordRepository extends JpaPlusRepository<LogRecord, Long> {

    // 方式1: 默认生成所有属性名查询
    List<LogRecord> findByType(String type);

    // 方式2: 内置属性表达式（如：And、Equals.....）
    List<LogRecord> findByTypeAndIp(String type, String ip);

    // 方式3: 内置注解查询、事务更新
    @Query(value = "select * from xx_log where time >= ?1", nativeQuery = true)
    List<LogRecord> findByTimeout(Long time);

    @Transactional
    @Modifying
    @Query(value = "update xx_log set time=?1 , where id =?2 ", nativeQuery = true)
    int updateTime(Long time, Long id);

    // 方式4: 内置QueryDsl
    Iterable<LogRecord> findAll(Predicate predicate);
}
```

## 注解

`@EnableJpaPlusRepositories` 
 
## 依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

## 扩展

`JpaPlusRepository` 整合了 `JpaRepositoryImplementation`、`QuerydslPredicateExecutor` 在保留原方法基础上，扩展了对`QueryDsl`的支持

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

    int update(T entity, Predicate... predicate);

    int delete(ID... ids);

    int delete(List<ID> ids);

    int delete(Predicate... predicate);

    ...
}
```

## 使用

```java
@EnableJpaPlusRepositories
@SpringBootApplication
public class FastBootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FastBootApplication.class);
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

## 其他

### 属性表达式

| 关键字           | 示例                          | 片段                                                    |
| ---------------- | ----------------------------- | ------------------------------------------------------- |
| And              | findByNameAndAge              | where x.name = ?1 and x.age = ?2                        |
| Or               | findByNameOrAge               | where x.name = ?1 or x.age = ?2                         |
| Is               | findByNameIs                  | where x.name = ?1                                       |
| Equals           | findByNameEquals              | where x.name = ?1                                       |
| Between          | findByStartDateBetween        | where x.startDate between ?1 and ?2                     |
| LessThan         | findByAgeLessThan             | where x.age < ?1                                        |
| LessThanEqual    | findByAgeLessThanEqual        | where x.age ⇐ ?1                                        |
| GreaterThan      | findByAgeGreaterThan          | where x.age > ?1                                        |
| GreaterThanEqual | findByAgeGreaterThanEqual     | where x.age >= ?1                                       |
| After            | findByStartDateAfter          | where x.startDate > ?1                                  |
| Before           | findByStartDateBefore         | where x.startDate < ?1                                  |
| IsNull           | findByAgeIsNull               | where x.age is null                                     |
| IsNotNull        | findByAgeIsNotNull            | where x.age not null                                    |
| NotNull          | findByAgeNotNull              | where x.age not null                                    |
| Like             | findByNameLike                | where x.name like ?1                                    |
| NotLike          | findByNameNotLike             | where x.name not like ?1                                |
| StartingWith     | findByNameStartingWith        | where x.name like ?1 (parameter bound with appended %)  |
| EndingWith       | findByNameEndingWith          | where x.name like ?1 (parameter bound with prepended %) |
| Containing       | findByNameContaining          | where x.name like ?1 (parameter bound wrapped in %)     |
| OrderBy          | findByAgeOrderByNameDesc      | where x.age = ?1 order by x.name desc                   |
| Not              | findByNameNot                 | where x.name <> ?1                                      |
| In               | findByAgeIn(Collectionages)   | where x.age in ?1                                       |
| NotIn            | findByAgeNotIn(Collectionage) | where x.age not in ?1                                   |
| TRUE             | findByActiveTrue()            | where x.active = true                                   |
| FALSE            | findByActiveFalse()           | where x.active = false                                  |
| IgnoreCase       | findByNameIgnoreCase          | where UPPER(x.name) = UPPER(?1)                         |

