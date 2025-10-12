在框架中持久层涉及到`Spring Jdbc`、`Spring Data Jpa`、`QueryDsl` 可单独用，混合用，或都不用

## Spring Jdbc

> 通用查询

```java
String sql = "select `id`, `name` from `xxx_member` order by `sort` asc limit 20;";
return mJdbcTemplatePlus.queryForMap(sql);
//return mJdbcTemplatePlus.queryForList(sql);
//return mJdbcTemplatePlus.queryForObj(sql, Member.class);
//return mJdbcTemplatePlus.queryForListObj(sql, Member.class);
```

## Spring Data Jpa

> 适用于日常操作。SDJ 是对 JPA 规范抽象，支持不同实现（如`Hibernate`）支持多种不同查询方式，更多见[官方文档](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)

```java
public interface MemberRepository extends JpaPlusRepository<Member, Long> {
    // 1.默认生成所有属性名查询
    Member findBy属性名(String status);

    // 2.内置属性表达式（如：And、Equals.....）
    Member findByUserNameAndPassword(String username,String username);
    Member findByUserNameEquals(String username);

     // 3.内置注解查询
    @Query(value = "select * from xxx_member where status=?1", nativeQuery = true)
    List<Member> findByStatus(String status);

    @Transactional
    @Modifying
    @Query(value = "update xxx_member set cros_ct=?1 , push_ct=?2 ", nativeQuery = true)
    int rest(Integer cros, Integer push);

    // 4.内置QueryDsl （重点拓展，如下方法已内置在JpaPlusRepository里）
    List<Member> findAll(Predicate predicate)
    List<Member> findAll(Predicate predicate, Sort sort);
    Page<Member> findAll(JPAQuery<Member> query, Pageable pageable)
    ....
}
```

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

## QueryDsl

> 适用于封装持久操作。面向对象查询框架，简洁封装，与`Spring Jpa`绝配，更多见[官方文档](http://www.querydsl.com/static/querydsl/latest/reference/html/index.html)

```java
QMember qm = QMember .member;
List<Member> memberList = queryFactory.selectFrom(qm)
    .where(qm.name.like('%'+"小明"+'%')
    .and(qm.address.contains("武汉"))
    .and(qm.status.eq("0"))
    .and(qm.age.between(20, 30)))
    .fetch();
```

## 其他

- 使用 `jdbc` 推荐 [querydsl](http://www.querydsl.com/)
- 使用 `mybatis` 推荐 [mybatis plus](https://mp.baomidou.com/)
- 使用 `hibernate` 推荐 [spring jpa](https://spring.io/projects/spring-data-jpa)
