# Spring Data Jpa

基于 **JPA** 标准，并提供了一套简洁的 API 和注解，封装 JPA 的复杂性，简化了数据层的开发工作，使开发人员能够更专注于业务逻辑的实现

```java
public interface LogRecordRepository extends JpaPlusRepository<LogRecord, Long> {

    // 方式1: 属性名
    List<LogRecord> findByType(String type);

    // 方式2: 属性表达式（如：And、Equals.....）
    List<LogRecord> findByTypeAndIp(String type, String ip);

    // 方式3: 内置注解查询、事务更新
    @Query(value = "select * from xx_log where time >= ?1", nativeQuery = true)
    List<LogRecord> findByTimeout(Long time);

    @Transactional
    @Modifying
    @Query(value = "update xx_log set time=?1 , where id =?2 ", nativeQuery = true)
    int updateTime(Long time, Long id);

    // 方式4: QueryDsl
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

    int update(Path<T> key, T val, Predicate... predicate);

    <V> int update(Path<V> key, Expression<? extends V> expression, Predicate... predicate);

    int delete(ID... ids);

    int delete(List<ID> ids);

    int delete(Predicate... predicate);
}
```

## 使用

```java
@Log4j2
@EnableJpaAuditing
@EnableJpaPlusRepositories
@SpringBootApplication
public class FastBootApplication {

    public static void main(String[] args) throws UnsupportedEncodingException {
        SpringApplication app = new SpringApplication(FastBootApplication.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        app.run(args);
        log.info("Started FastBootApplication Successfully");
    }
}
```

## 示例

`QLogRecord ql = QLogRecord.logRecord;`

```java
public class JpaPlusPojo {

    @Data
    @AllArgsConstructor
    public static class LogRecordPojo {

        public Long id;

        public String ip;

        public Long min;

        public Long max;
    }
}
```

### 查询

#### 单表

```java
Test
@Order(1)
public void select() {
    // 条件
    Predicate predicate = ql.type.likeIgnoreCase("%GET%");
    // 排序
    Sort sort = Sort.by(Direction.ASC, LogRecord.FIELDS.createDate);
    // 分页
    Pageable pageable = PageRequest.of(0, 10, sort);

    // 分页查询
    Expression<LogRecord> exp = ql;
    JPAQuery<LogRecord> jpaQuery = mJpaQuery.select(exp).from(ql).where(predicate);
    Page<LogRecord> data = mLogRecordRepository.findAll(jpaQuery, pageable);
    assertEquals(data.getContent().size(), 6);

    // 分页查询
    Expression<LogRecord> expFields = Projections.fields(LogRecord.class, ql, ql.id, ql.ip);
    jpaQuery.select(expFields);
    data = mLogRecordRepository.findAll(jpaQuery, pageable);
    assertEquals(data.getContent().size(), 6);

    // 投影查询（多表联合查询）
    Expression expTuple =Projections.constructor(LogRecordPojo.class, ql.id, ql.ip, ql.time.min(), ql.time.max());
    List<LogRecordPojo> list = jpaQuery.select(expTuple).from(ql).groupBy(ql.type, ql.createDate).fetch();
    assertTrue(list.size() > 0);
}
```

#### 多表

```java
@RequestMapping(value = "page")
public PaginationResult page(BaseVo base, PaginationVo page) {
    // 对象
    QUser qUser = QUser.user;
    QSRole qRole = QSRole.sRole;
    QUserRole qUserRole = QUserRole.userRole;
    // 条件
    Predicate predicate = qUser.id.isNotNull();
    if (StrUtil.isNotBlank(base.getKeyword())) {
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

### 新增

```java
@Test
@Order(3)
public void insertOrUpdate() {
    // 单个
    LogRecord lr11 = result.get(0);
    lr11.setTime(100L);
    LogRecord lr12 = mLogRecordRepo.insertOrUpdate(lr11);
    assertEquals(lr12.getTime(), 100);
    lr12.setTime(101L);
    int row = mLogRecordRepo.insertOrUpdateRow(lr12);
    assertEquals(row, 1);

    // 多个
    LogRecord lr21 = result.get(0);
    lr21.setTime(102L);
    LogRecord lr22 = result.get(1);
    lr22.setTime(103L);
    List<LogRecord> lrs3 = mLogRecordRepo.insertOrUpdate(lr21, lr22);
    assertEquals(lrs3.get(0).getTime(), 102);
    assertEquals(lrs3.get(1).getTime(), 103);
    lr21.setTime(104L);
    lr22.setTime(104L);
    row = mLogRecordRepo.insertOrUpdateRow(lr21, lr22);
    assertEquals(row, 2);
}
```

### 更新
```java
@Test
@Order(4)
public void update() {
    LogRecord lr = result.get(0);
    Long id = lr.getId();
    Predicate predicate = ql.id.eq(id);

    lr.setTime(101L);
    int row = mLogRecordRepo.update(lr, predicate);
    assertTrue(row > 0);
    assertEquals(mLogRecordRepo.findOne(id).getTime(), 101);

    lr.setTime(102L);
    row = mLogRecordRepo.update(ql, lr, predicate);
    assertTrue(row > 0);
    assertEquals(mLogRecordRepo.findOne(id).getTime(), 102);

    row = mLogRecordRepo.update(ql.time, Expressions.constant(103L), predicate);
    assertTrue(row > 0);
    assertEquals(mLogRecordRepo.findOne(id).getTime(), 103);
}
```

### 删除

```java
@Test
@Order(3)
public void delete() {
    // 单个
    mLogRecordRepo.delete(result.get(0).getId(), result.get(1).getId());

    // 多个
    int row = mLogRecordRepo.delete(ql.type.eq("GET"));
    assertTrue(row > 0);
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
