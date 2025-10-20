package com.xiesx.fastboot.db.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.core.exception.RunException;
import java.util.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class JdbcTemplatePlusTest {

  @Mock NamedParameterJdbcTemplate jdbcTemplate;

  JdbcTemplatePlus cls;

  @BeforeEach
  public void setup() throws Exception {
    java.lang.reflect.Field field = JdbcTemplatePlus.class.getDeclaredField("jdbcTemplate");
    field.setAccessible(true);
    field.set(null, jdbcTemplate);

    cls = new JdbcTemplatePlus();
  }

  @Test
  void testConstructor() {
    assertNotNull(cls);
  }

  @Test
  @Order(1)
  public void testQueryForMap_noParam() {
    Map<String, Object> mockResult = Maps.newHashMap();
    mockResult.put("id", 1);
    when(jdbcTemplate.queryForMap(anyString(), any(SqlParameterSource.class)))
        .thenReturn(mockResult);

    Map<String, Object> result = JdbcTemplatePlus.queryForMap("SELECT * FROM test");
    assertNotNull(result);
    assertEquals(1, result.get("id"));
  }

  @Test
  @Order(2)
  public void testQueryForMap_withParam() {
    Map<String, Object> mockResult = Maps.newHashMap();
    mockResult.put("name", "Alice");
    when(jdbcTemplate.queryForMap(anyString(), any(SqlParameterSource.class)))
        .thenReturn(mockResult);

    Map<String, Object> param = Maps.newHashMap();
    param.put("id", 1);

    Map<String, Object> result =
        JdbcTemplatePlus.queryForMap("SELECT * FROM test WHERE id=:id", param);
    assertNotNull(result);
    assertEquals("Alice", result.get("name"));
  }

  @Test
  @Order(3)
  public void testQueryForList_noParam() {
    List<Map<String, Object>> mockList = Lists.newArrayList();
    Map<String, Object> row = Maps.newHashMap();
    row.put("id", 1);
    mockList.add(row);

    when(jdbcTemplate.queryForList(anyString(), any(SqlParameterSource.class)))
        .thenReturn(mockList);

    List<Map<String, Object>> result = JdbcTemplatePlus.queryForList("SELECT * FROM test");
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(1, result.get(0).get("id"));
  }

  @Test
  @Order(4)
  public void testUpdate_noParam() {
    when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);

    int result = JdbcTemplatePlus.update("UPDATE test SET name='Bob'");
    assertEquals(1, result);
  }

  @Test
  @Order(5)
  public void testBatchUpdate() {
    List<Map<String, Object>> data = Lists.newArrayList();
    Map<String, Object> row = Maps.newHashMap();
    row.put("id", 1);
    data.add(row);

    when(jdbcTemplate.batchUpdate(anyString(), any(SqlParameterSource[].class)))
        .thenReturn(new int[] {1});

    int result = JdbcTemplatePlus.batchUpdate("UPDATE test SET name='Bob' WHERE id=:id", data);
    assertEquals(1, result);
  }

  @Test
  @Order(6)
  public void testQueryForMap_exception() {
    // 模拟 queryForMap 抛异常
    when(jdbcTemplate.queryForMap(anyString(), any(SqlParameterSource.class)))
        .thenThrow(new RuntimeException("DB error"));

    Map<String, Object> result = JdbcTemplatePlus.queryForMap("SELECT * FROM test");
    assertNotNull(result);
    assertTrue(result.isEmpty(), "异常情况下应返回空 Map");
  }

  @Test
  @Order(7)
  public void testQueryForList_exception() {
    // 模拟 queryForList 抛异常
    when(jdbcTemplate.queryForList(anyString(), any(SqlParameterSource.class)))
        .thenThrow(new RuntimeException("DB error"));

    List<Map<String, Object>> result = JdbcTemplatePlus.queryForList("SELECT * FROM test");
    assertNotNull(result);
    assertTrue(result.isEmpty(), "异常情况下应返回空 List");
  }

  @Test
  @Order(8)
  public void testUpdate_exception() {
    // 模拟 update 抛异常
    when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class)))
        .thenThrow(new RuntimeException("DB error"));

    int result = JdbcTemplatePlus.update("UPDATE test SET name='Bob'");
    assertEquals(0, result, "异常情况下应返回 0");
  }

  @Test
  @Order(9)
  public void testBatchUpdate_empty() {
    int result =
        JdbcTemplatePlus.batchUpdate(
            "UPDATE test SET name='Bob' WHERE id=:id", Lists.newArrayList());
    assertEquals(0, result);
  }

  @Test
  @Order(10)
  public void testBatchUpdate_exception() {
    // 模拟 batchUpdate 抛异常
    when(jdbcTemplate.batchUpdate(anyString(), any(SqlParameterSource[].class))).thenThrow();

    List<Map<String, Object>> data = Lists.newArrayList();
    Map<String, Object> row = Maps.newHashMap();
    row.put("id", 1);
    data.add(row);

    int result = JdbcTemplatePlus.batchUpdate("UPDATE test SET name='Bob' WHERE id=:id", data);
    assertEquals(-1, result, "异常情况下应返回 0");
  }

  @Test
  @Order(11)
  public void testGetJdbcTemplate_null() throws Exception {
    JdbcTemplatePlus.setJdbcTemplate(null);
    assertThrows(RunException.class, () -> JdbcTemplatePlus.get());
  }
}
