# QueryDsl

 **灵活、高效** 面向对象查询的方式，像写代码一样写 SQL，可靠地构建动态查询

## 依赖

```xml
<dependency>
	<groupId>com.querydsl</groupId>
	<artifactId>querydsl-jpa</artifactId>
</dependency>
<dependency>
	<groupId>com.querydsl</groupId>
	<artifactId>querydsl-apt</artifactId>
	<scope>provided</scope>
</dependency>

<build>
	<plugins>
		<plugin>
			<groupId>com.mysema.maven</groupId>
			<artifactId>apt-maven-plugin</artifactId>
			<version>1.1.3</version>
			<executions>
				<execution>
					<goals>
						<goal>process</goal>
					</goals>
					<configuration>
						<outputDirectory>target/generated-sources/java</outputDirectory>
						<processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
					</configuration>
				</execution>
			</executions>
			<dependencies>
				<dependency>
					<groupId>com.querydsl</groupId>
					<artifactId>querydsl-apt</artifactId>
					<version>${querydsl.version}</version>
				</dependency>
			</dependencies>
		</plugin>
	</plugins>
</build>
```

配置后，新建实体类为`LogRecord`，会自动生成`QLogRecord`，支持手动编写（已测试）

::: code-group

```java [LogRecord.java]
@Data
@Accessors(chain = true)
@FieldNameConstants(innerTypeName = "FIELDS")
@Table(name = "xx_log")
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@SQLRestriction("del=0")
@SQLDelete(sql = "update xx_log set del=1 where id = ?")
public class LogRecord extends JpaPlusEntity<LogRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，例（L1408447004119666688）
     */
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GeneratedIdWorker(centerId = 1,workerId = 0)
    @JSONField(ordinal = 1)
    private Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(updatable = false, nullable = false)
    @JSONField(ordinal = 2)
    private Date createDate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(nullable = false)
    @JSONField(ordinal = 3)
    private Date updateDate;

    /**
     * 请求IP
     */
    @Column
    @JSONField(ordinal = 4)
    private String ip;

    /**
     * 方法
     */
    @Column
    @JSONField(ordinal = 5)
    private String method;

    /**
     * 方式
     */
    @Column
    @JSONField(ordinal = 6)
    private String type;

    /**
     * 地址
     */
    @Column
    @JSONField(ordinal = 7)
    private String url;

    /**
     * 请求参数
     */
    @JSONField(serialize = false)
    @Lob
    private String req;

    /**
     * 响应结果
     */
    @Lob
    @JSONField(serialize = false)
    private String res;

    /**
     * 执行时间（毫秒）
     */
    @Column
    @JSONField(ordinal = 10)
    private Long time;

    /**
     * 是否删除
     */
    @Column
    @JSONField(serialize = false)
    private boolean del = false;

    // ======================

    @JSONField(serialize = false, ordinal = 8)
    public Object getParams() {
        return JSON.parse(req);
    }

    @JSONField(serialize = false, ordinal = 9)
    public Object getResult() {
        return JSON.parse(res);
    }
}
```

```java [QLogRecord.java]
@Generated("com.querydsl.codegen.EntitySerializer")
public class QLogRecord extends EntityPathBase<LogRecord> {

    private static final long serialVersionUID = -998651177L;

    public static final QLogRecord logRecord = new QLogRecord("logRecord");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath ip = createString("ip");

    public final BooleanPath del = createBoolean("del");

    public final StringPath method = createString("method");

    public final StringPath req = createString("req");

    public final StringPath res = createString("res");

    public final NumberPath<Long> time = createNumber("time", Long.class);

    public final StringPath type = createString("type");

    public final DateTimePath<java.util.Date> updateDate = createDateTime("updateDate", java.util.Date.class);

    public final StringPath url = createString("url");

    public QLogRecord(String variable) {
        super(LogRecord.class, forVariable(variable));
    }

    public QLogRecord(Path<? extends LogRecord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLogRecord(PathMetadata metadata) {
        super(LogRecord.class, metadata);
    }
}
```

:::

## 使用

```java
@Autowired JPAQueryFactory mJPAQuery;
```

## 示例

`QLogRecord ql = QLogRecord.logRecord;`

### 查询

```java
@Test
@Order(1)
public void select() {
    List<LogRecord> list = mJPAQuery.selectFrom(ql).orderBy(ql.time.desc()).fetch();;
    assertEquals(list.size(), 13);
}
```

### 子查询

```java
@Test
@Order(2)
public void sub() {
    List<LogRecord> list = mJPAQuery.selectFrom(ql)
                    .where(ql.time.gt(JPAExpressions.select(ql.time.avg()).from(ql)))
                    .fetch();
    assertEquals(list.size(), 6);
}
```

### 条件

```java
@Test
@Order(3)
public void where() {
    LogRecord lr = mJPAQuery.selectFrom(ql)
                .where(ql.method.eq("test"), ql.type.eq("GET"))
                .orderBy(ql.time.desc())
                .fetchFirst();
    assertEquals(lr.getType(), "GET");
}
```

### 聚合

```java
@Test
@Order(4)
public void aggregate() {
    List<Tuple> list = mJPAQuery.select(ql.type, ql.id.count().as("ct"))
                .from(ql)
                .groupBy(ql.type)
                .fetch();
    assertFalse(list.isEmpty());
    assertEquals(list.get(0).get(1, Long.class), 6);
    assertEquals(list.get(1).get(1, Long.class), 7);
}
```

### 投影

```java
@Test
@Order(5)
public void tuple() {
    ConstructorExpression<LogRecordPojo> expression =Projections.constructor(LogRecordPojo.class,
                    ql.id,
                    ql.ip,
                    ql.type,
                    ql.time,
                    ql.time.min(),
                    ql.time.max());
    List<LogRecordPojo> list = mJPAQuery.select(expression).from(ql).groupBy(ql.type).fetch();
    assertEquals(list.size(), 2);
}
}
```

### 添加

建议使用 `Repository` 方法更新

```java
@Test
@Order(6)
public void save() {
    LogRecord lr = result.get(0);
    lr.setTime(100L);
    LogRecord lr2 = mLogRecordRepository.saveAndFlush(lr);
    assertEquals(lr2.getTime(), 100L);
}
```

### 修改

```java
@Test
@Order(7)
public void update() {
    LogRecord lr = result.get(0);
    long row = mJPAQuery.update(ql).set(ql.ip, "localhost").where(ql.id.in(lr.getId())).execute();
    assertEquals(row, 1L);
}
```

### 删除

```java
@Test
@Order(8)
public void delete() {
    List<Long> ids = result.stream().map(LogRecord::getId).toList();
    long row = mJPAQuery.delete(ql).where(ql.id.in(ids)).execute();
    assertEquals(row, ids.size());
}
```
