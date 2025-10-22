package com.xiesx.fastboot.db.jdbc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
public class JdbcTemplatePlus {

  private static NamedParameterJdbcTemplate jdbcTemplate;

  public static NamedParameterJdbcTemplate get() {
    if (jdbcTemplate == null) {
      try {
        jdbcTemplate = SpringHelper.getBean(NamedParameterJdbcTemplate.class);
      } catch (Exception e) {
        throw new RunException(
            RunExc.DBASE,
            "Spring JDBC dependency is missing. Please add 'spring-jdbc' to your pom.xml.");
      }
    }
    return jdbcTemplate;
  }

  static void setJdbcTemplate(NamedParameterJdbcTemplate template) {
    jdbcTemplate = template;
  }

  /* ------------------- 查询方法 ------------------- */

  public static Map<String, Object> queryForMap(String sql) {
    return executeQuery(sql, null, ps -> get().queryForMap(sql, ps), Maps.newConcurrentMap());
  }

  public static Map<String, Object> queryForMap(String sql, Object obj) {
    return executeQuery(sql, obj, ps -> get().queryForMap(sql, ps), Maps.newConcurrentMap());
  }

  public static <T> T queryForMap(String sql, Class<T> cla) {
    Map<String, Object> map = queryForMap(sql);
    return map.isEmpty() ? null : result(map, cla);
  }

  public static <T> T queryForMap(String sql, Object obj, Class<T> cla) {
    Map<String, Object> map = queryForMap(sql, obj);
    return map.isEmpty() ? null : result(map, cla);
  }

  // ---------

  public static List<Map<String, Object>> queryForList(String sql) {
    return executeQuery(sql, null, ps -> get().queryForList(sql, ps), Lists.newArrayList());
  }

  public static List<Map<String, Object>> queryForList(String sql, Object obj) {
    return executeQuery(sql, obj, ps -> get().queryForList(sql, ps), Lists.newArrayList());
  }

  public static <T> List<T> queryForList(String sql, Class<T> cla) {
    List<Map<String, Object>> list = queryForList(sql);
    return list.stream().map(m -> result(m, cla)).collect(Collectors.toList());
  }

  public static <T> List<T> queryForList(String sql, Object obj, Class<T> cla) {
    List<Map<String, Object>> list = queryForList(sql, obj);
    return list.stream().map(m -> result(m, cla)).collect(Collectors.toList());
  }

  private static <R> R executeQuery(
      String sql, Object param, QueryHandler<R> handler, R defaultValue) {
    try {
      SqlParameterSource ps = param != null ? parameter(param) : new MapSqlParameterSource();
      return handler.handle(ps);
    } catch (Exception e) {
      log.error("SQL execution error: {}\nSQL: {}", e.getMessage(), sql, e);
      return defaultValue;
    }
  }

  /* ------------------- 更新方法 ------------------- */

  @Transactional
  public static int update(String sql) {
    return executeUpdate(sql, null);
  }

  public static int update(String sql, Object obj) {
    return executeUpdate(sql, obj);
  }

  @Transactional
  public static int batchUpdate(String sql, List<?> data) {
    if (data == null || data.isEmpty()) return 0;
    try {
      int[] results = get().batchUpdate(sql, SqlParameterSourceUtils.createBatch(data));
      return results.length;
    } catch (Exception e) {
      log.error("Batch update error: {}\nSQL: {}", e.getMessage(), sql, e);
      return -1;
    }
  }

  private static int executeUpdate(String sql, Object param) {
    return executeQuery(sql, param, ps -> get().update(sql, ps), 0);
  }

  /* ------------------- 工具方法 ------------------- */

  private static SqlParameterSource parameter(Object obj) {
    if (obj == null) return new MapSqlParameterSource();
    if (Map.class.isAssignableFrom(obj.getClass())) {
      return new MapSqlParameterSource(Convert.toMap(String.class, Object.class, obj));
    }
    return new BeanPropertySqlParameterSource(obj);
  }

  private static <T> T result(Map<String, Object> map, Class<T> cla) {
    if (MapUtil.isEmpty(map)) return null;
    return BeanUtil.toBean(
        map,
        cla,
        CopyOptions.create().setIgnoreCase(true).setIgnoreError(true).setIgnoreNullValue(true));
  }

  @FunctionalInterface
  private interface QueryHandler<R> {
    R handle(SqlParameterSource ps) throws Exception;
  }
}
