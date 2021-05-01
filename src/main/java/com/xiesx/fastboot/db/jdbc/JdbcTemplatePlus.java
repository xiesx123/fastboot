package com.xiesx.fastboot.db.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.hutool.core.map.MapUtil;
import lombok.extern.log4j.Log4j2;

/**
 * @title JdbcPlusTemplate.java
 * @description
 * @author xiesx
 * @date 2020-7-21 22:32:10
 */
@Log4j2
@Component
public class JdbcTemplatePlus {

    /** 持久对象 */
    @Autowired
    public NamedParameterJdbcTemplate mNamedParameterJdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return mNamedParameterJdbcTemplate.getJdbcTemplate();
    }

    /**
     * 查询Map
     *
     * @param sql
     * @param args
     * @return
     */
    public Map<String, Object> queryForMap(String sql) {
        try {
            return mNamedParameterJdbcTemplate.queryForMap(sql, Maps.newHashMap());
        } catch (Exception e) {
            log.error("query for map error", e);
            return null;
        }
    }

    public Map<String, Object> queryForMap(String sql, Object paramDto) {
        try {
            return mNamedParameterJdbcTemplate.queryForMap(sql, new BeanPropertySqlParameterSource(paramDto));
        } catch (Exception e) {
            log.error("query for map error", e);
            return null;
        }
    }

    public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap) {
        try {
            return mNamedParameterJdbcTemplate.queryForMap(sql, paramMap);
        } catch (Exception e) {
            log.error("query for map error", e);
            return null;
        }
    }

    public <T> T queryForMap(String sql, Class<T> clazz) {
        try {
            return result(queryForMap(sql, Maps.newHashMap()), clazz);
        } catch (Exception e) {
            log.error("query for map error", e);
            return null;
        }
    }

    public <T> T queryForMap(String sql, Object paramDto, Class<T> clazz) {
        try {
            return result(queryForMap(sql, paramDto), clazz);
        } catch (Exception e) {
            log.error("query for map error", e);
            return null;
        }
    }

    public <T> T queryForMap(String sql, Map<String, ?> paramMap, Class<T> clazz) {
        try {
            return result(queryForMap(sql, paramMap), clazz);
        } catch (Exception e) {
            log.error("query for map error", e);
            return null;
        }
    }

    /**
     * 查询List
     *
     * @param sql
     * @param args
     * @return
     */
    public List<Map<String, Object>> queryForList(String sql) {
        try {
            return mNamedParameterJdbcTemplate.queryForList(sql, Maps.newHashMap());
        } catch (Exception e) {
            log.error("query for list error", e);
            return null;
        }
    }

    public List<Map<String, Object>> queryForList(String sql, Object paramDto) {
        try {
            return mNamedParameterJdbcTemplate.queryForList(sql, new BeanPropertySqlParameterSource(paramDto));
        } catch (Exception e) {
            log.error("query for list error", e);
            return null;
        }
    }

    public List<Map<String, Object>> queryForList(String sql, Map<String, ?> paramMap) {
        try {
            return mNamedParameterJdbcTemplate.queryForList(sql, paramMap);
        } catch (Exception e) {
            log.error("query for list error", e);
            return null;
        }
    }

    public <T> List<T> queryForList(String sql, Class<T> clazz) {
        try {
            return result(queryForList(sql, Maps.newHashMap()), clazz);
        } catch (Exception e) {
            log.error("query for list error", e);
            return null;
        }
    }

    public <T> List<T> queryForList(String sql, Object paramDto, Class<T> clazz) {
        try {
            return result(queryForList(sql, paramDto), clazz);
        } catch (Exception e) {
            log.error("query for list error", e);
            return null;
        }
    }

    public <T> List<T> queryForList(String sql, Map<String, ?> paramMap, Class<T> clazz) {
        try {
            return result(queryForList(sql, paramMap), clazz);
        } catch (Exception e) {
            log.error("query for list error", e);
            return null;
        }
    }

    /**
     * 更新update
     *
     * @param sql
     * @param args
     * @return
     */
    public int update(String sql) {
        try {
            return mNamedParameterJdbcTemplate.update(sql, Maps.newHashMap());
        } catch (Exception e) {
            log.error("update error", e);
            return 0;
        }
    }

    public int update(String sql, Object paramDto) {
        try {
            return mNamedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(paramDto));
        } catch (Exception e) {
            log.error("update error", e);
            return 0;
        }
    }

    public int update(String sql, Map<String, ?> paramMap) {
        try {
            return mNamedParameterJdbcTemplate.update(sql, paramMap);
        } catch (Exception e) {
            log.error("update error", e);
            return 0;
        }
    }

    //

    /**
     * 数据填充
     *
     * @param map
     * @return
     */
    private <T> T result(Map<String, Object> map, Class<T> clazz) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        return JSON.toJavaObject(new JSONObject(map), clazz);
    }

    private <T> List<T> result(List<Map<String, Object>> list, Class<T> clazz) {
        List<T> data = Lists.newArrayList();
        for (Map<String, Object> map : list) {
            data.add(result(map, clazz));
        }
        return data;
    }
}
