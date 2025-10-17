package com.xiesx.fastboot.db.jdbc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;

import lombok.extern.log4j.Log4j2;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Log4j2
public class JdbcTemplatePlus {

    public static NamedParameterJdbcTemplate get() {
        NamedParameterJdbcTemplate mNamedParameterJdbcTemplate =
                SpringHelper.getBean(NamedParameterJdbcTemplate.class);
        Assert.notNull(
                mNamedParameterJdbcTemplate,
                () -> new RunException(RunExc.DBASE, "pom need dependency spring-jdbc"));
        return mNamedParameterJdbcTemplate;
    }

    /** 查询Map */
    public static Map<String, Object> queryForMap(String sql) {
        try {
            return get().queryForMap(sql, Maps.newConcurrentMap());
        } catch (Exception e) {
            log.error("query for map error {}", e.getMessage());
            return Maps.newConcurrentMap();
        }
    }

    public static Map<String, Object> queryForMap(String sql, Object obj) {
        try {
            return get().queryForMap(sql, parameter(obj));
        } catch (Exception e) {
            log.error("query for map error {}", e.getMessage());
            return Maps.newConcurrentMap();
        }
    }

    public static <T> T queryForMap(String sql, Class<T> cla) {
        try {
            return result(queryForMap(sql, Maps.newConcurrentMap()), cla);
        } catch (Exception e) {
            log.error("query for map error {}", e.getMessage());
            return null;
        }
    }

    public static <T> T queryForMap(String sql, Object obj, Class<T> cla) {
        try {
            return result(queryForMap(sql, obj), cla);
        } catch (Exception e) {
            log.error("query for map error {}", e.getMessage());
            return null;
        }
    }

    /** 查询List */
    public static List<Map<String, Object>> queryForList(String sql) {
        try {
            return get().queryForList(sql, Maps.newConcurrentMap());
        } catch (Exception e) {
            log.error("query for list error {}", e.getMessage());
            return Lists.newArrayList();
        }
    }

    public static List<Map<String, Object>> queryForList(String sql, Object obj) {
        try {
            return get().queryForList(sql, parameter(obj));
        } catch (Exception e) {
            log.error("query for list error {}", e.getMessage());
            return Lists.newArrayList();
        }
    }

    public static <T> List<T> queryForList(String sql, Class<T> cla) {
        try {
            return result(queryForList(sql, Maps.newConcurrentMap()), cla);
        } catch (Exception e) {
            log.error("query for list error {}", e.getMessage());
            return Lists.newArrayList();
        }
    }

    public static <T> List<T> queryForList(String sql, Object obj, Class<T> cla) {
        try {
            return result(queryForList(sql, obj), cla);
        } catch (Exception e) {
            log.error("query for list error {}", e.getMessage());
            return Lists.newArrayList();
        }
    }

    /** 插入、更新、删除 */
    @Transactional
    public static int update(String sql) {
        try {
            return get().update(sql, Maps.newConcurrentMap());
        } catch (Exception e) {
            log.error("update error {}", e.getMessage());
            return 0;
        }
    }

    @Transactional
    public static int update(String sql, Object obj) {
        try {
            return get().update(sql, parameter(obj));
        } catch (Exception e) {
            log.error("update error {}", e.getMessage());
            return 0;
        }
    }

    /** 插入、更新、删除（批量） */
    @Transactional
    public static int batchUpdate(String sql, List<?> data) {
        try {
            return get().batchUpdate(sql, SqlParameterSourceUtils.createBatch(data)).length;
        } catch (Exception e) {
            log.error("batch update error {}", e.getMessage());
            return 0;
        }
    }

    /** 参数填充 */
    private static SqlParameterSource parameter(Object obj) {
        if (obj instanceof Map) {
            return new MapSqlParameterSource(Convert.toMap(String.class, Object.class, obj));
        }
        return new BeanPropertySqlParameterSource(obj);
    }

    /** 数据填充 */
    private static <T> T result(Map<String, Object> map, Class<T> cla) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        return BeanUtil.toBean(
                map,
                cla,
                CopyOptions.create()
                        .setIgnoreCase(true)
                        .setIgnoreError(true)
                        .setIgnoreNullValue(true));
    }

    private static <T> List<T> result(List<Map<String, Object>> list, Class<T> cla) {
        List<T> data = Lists.newArrayList();
        list.forEach(
                map -> {
                    data.add(result(map, cla));
                });
        return data;
    }
}
