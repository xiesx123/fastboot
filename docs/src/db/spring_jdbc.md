# Spring Jdbc

xxx

## 依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>
```

## 扩展

```java
public static Map<String, Object> queryForMap(String sql)
public static Map<String, Object> queryForMap(String sql, Object obj)
public static <T> T queryForMap(String sql, Class<T> cla)
public static <T> T queryForMap(String sql, Object obj, Class<T> cla)

public static List<Map<String, Object>> queryForList(String sql)
public static List<Map<String, Object>> queryForList(String sql, Object obj)
public static <T> List<T> queryForList(String sql, Class<T> cla)
public static <T> List<T> queryForList(String sql, Object obj, Class<T> cla)

public static int update(String sql)
public static int update(String sql, Object obj)
public static int batchUpdate(String sql, List<?> data)
```

## 示例

```java
public class JdbcPlusPojo {

    @Data
    @Accessors(chain = true)
    public static class LogRecordPojo {

        public String id;

        public String ip;

        public String type;

        public Long time;
    }

    @Data
    public static class CommonPojo {

        public Long ct;
    }
}
```

### 查询

#### 对象

```java
@Test
@Order(1)
public void map() {
    String sql = "SELECT COUNT(1) AS ct FROM xx_log";
    // map
    Map<String, Object> r1 = JdbcTemplatePlus.queryForMap(sql);
    // bean
    CommonPojo r2 = JdbcTemplatePlus.queryForMap(sql, CommonPojo.class);
    assertEquals(r1.getOrDefault("ct", 0), r2.getCt());
}

@Test
@Order(2)
public void map2() {
    String sql = "SELECT id, TYPE FROM xx_log WHERE ip = :ip AND type = :type";
    LogRecordPojo pojo = new LogRecordPojo().setIp("127.0.1.1").setType("GET");
    Map<String, Object> map = BeanUtil.beanToMap(pojo);
    // map
    Map<String, Object> r11 = JdbcTemplatePlus.queryForMap(sql, pojo);
    Map<String, Object> r12 = JdbcTemplatePlus.queryForMap(sql, map);
    assertEquals(r11.get("type"), r12.get("type"));
    // bean
    LogRecordPojo r21 = JdbcTemplatePlus.queryForMap(sql, pojo, LogRecordPojo.class);
    LogRecordPojo r22 = JdbcTemplatePlus.queryForMap(sql, map, LogRecordPojo.class);
    assertEquals(r21.getType(), r22.getType());

    assertEquals(r11.get("type"), r21.getType());
}
```

#### 数组

```java
@Test
@Order(3)
public void list() {
    String sql = "SELECT * FROM xx_log LIMIT 10";
    // list map
    List<Map<String, Object>> r1 = JdbcTemplatePlus.queryForList(sql);
    // list bean
    List<LogRecordPojo> r2 = JdbcTemplatePlus.queryForList(sql, LogRecordPojo.class);
    assertEquals(r1.get(0).get("type"), r2.get(0).getType());
}

@Test
@Order(4)
public void list2() {
    String sql = "SELECT id, TYPE FROM xx_log WHERE type = :type";
    LogRecordPojo pojo = new LogRecordPojo().setType("GET");
    Map<String, Object> params = BeanUtil.beanToMap(pojo);
    // list map
    List<Map<String, Object>> r11 = JdbcTemplatePlus.queryForList(sql, pojo);
    List<Map<String, Object>> r12 = JdbcTemplatePlus.queryForList(sql, params);
    assertEquals(r11.size(), r12.size());
    // list bean
    List<LogRecordPojo> r21 = JdbcTemplatePlus.queryForList(sql, pojo, LogRecordPojo.class);
    List<LogRecordPojo> r22 = JdbcTemplatePlus.queryForList(sql, params, LogRecordPojo.class);
    assertEquals(r21.size(), r22.size());

    assertEquals(r11.get(0).get("type"), r21.get(0).getType());
}
```
 
### 添加

```java
@Test
@Order(5)
public void insert() {
    String sql =
            "insert into `xx_log` (`id`, `create_date`, `update_date`, `ip`, `method`, `type`,"
                    + " `url`, `req`, `res`, `time`) values(:id,  :createDate,  now(),  :ip, "
                    + " :method,  :type,  :url,  :req,  :res,  :time);";
    // bean
    LogRecord logRecord =
            new LogRecord()
                    .setId(IdWorkerGenerator.nextId())
                    .setIp("127.0.0.1")
                    .setCreateDate(DateUtil.date())
                    .setUpdateDate(DateUtil.date())
                    .setMethod("test")
                    .setType("GET");
    assertEquals(JdbcTemplatePlus.update(sql, logRecord), 1);

    // map
    Map<String, Object> map = BeanUtil.beanToMap(logRecord);
    map.put("id", IdWorkerGenerator.nextId());
    map.put("type", "POST");
    assertEquals(JdbcTemplatePlus.update(sql, map), 1);

    // batch
    result.forEach(
            item -> {
                item.setId(IdWorkerGenerator.nextId());
                item.setTime(4L);
            });
    assertEquals(JdbcTemplatePlus.batchUpdate(sql, result), 10);
}
```
 
### 修改

```java
@Test
@Order(6)
public void update() {
    String sql = "UPDATE xx_log SET time = :time WHERE id = :id";
    
    // bean
    LogRecord logRecord = result.get(0);
    logRecord.setTime(2L);
    assertNotNull(logRecord.getId());
    assertEquals(JdbcTemplatePlus.update(sql, logRecord), 1);

    // map
    Map<String, Object> map = BeanUtil.beanToMap(result.get(1));
    map.put("type", "POST");
    assertNotNull(map.get(LogRecord.FIELDS.id));
    assertEquals(JdbcTemplatePlus.update(sql, map), 1);

    // batch
    result.forEach(
            item -> {
                item.setTime(4L);
            });
    assertEquals(JdbcTemplatePlus.batchUpdate(sql, result), 10);
}
```

### 删除

```java
@Test
@Order(7)
public void delete() {
    String sql = "SELECT COUNT(1) AS ct FROM xx_log  WHERE type = 'GET'";
    int ct = MapUtil.getInt(JdbcTemplatePlus.queryForMap(sql), "ct");
    assertEquals(JdbcTemplatePlus.update("DELETE FROM xx_log WHERE type = 'GET'"), ct);
}
```