package com.xiesx.fastboot.db.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.db.jdbc.pojo.CommonPojo;
import com.xiesx.fastboot.db.jdbc.pojo.RegionPojo;

import cn.hutool.core.bean.BeanUtil;

/**
 * @title JdbcTemplatePlusTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:01
 */
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class)
public class JdbcTemplatePlusTest {

    @Test
    @Order(1)
    public void map() {
        // 查询总数
        String sql = "SELECT COUNT(1) AS ct FROM sys_region";
        // 输出map
        Map<String, Object> result1 = JdbcTemplatePlus.queryForMap(sql);
        // 输出obj
        CommonPojo result2 = JdbcTemplatePlus.queryForMap(sql, CommonPojo.class);
        // 验证
        assertEquals(result1.getOrDefault("ct", 0), result2.getCt());
    }

    @Test
    @Order(2)
    public void mapObj() {
        // 查询主键
        String sql = "SELECT id, NAME FROM sys_region WHERE id = :id";
        // 入参obj
        RegionPojo pojo = new RegionPojo().setId(1);
        // 入参map
        Map<String, Object> params = BeanUtil.beanToMap(pojo);
        // 输出map
        Map<String, Object> result11 = JdbcTemplatePlus.queryForMap(sql, pojo);
        Map<String, Object> result12 = JdbcTemplatePlus.queryForMap(sql, params);
        assertEquals(result11.get(RegionPojo.FIELDS.name), result12.get(RegionPojo.FIELDS.name));
        // 输出obj
        RegionPojo result21 = JdbcTemplatePlus.queryForMap(sql, pojo, RegionPojo.class);
        RegionPojo result22 = JdbcTemplatePlus.queryForMap(sql, params, RegionPojo.class);
        assertEquals(result21.getName(), result22.getName());
        // 验证
        assertEquals(result11.get(RegionPojo.FIELDS.name), result22.getName());
    }

    @Test
    @Order(3)
    public void list() {
        // 查询10个
        String sql = "SELECT * FROM sys_region LIMIT 10";
        // 输出List<Map>
        List<Map<String, Object>> result1 = JdbcTemplatePlus.queryForList(sql);
        // 输出List<Obj>
        List<RegionPojo> result2 = JdbcTemplatePlus.queryForList(sql, RegionPojo.class);
        // 验证
        assertEquals(result1.get(9).get(RegionPojo.FIELDS.name), result2.get(9).getName());
    }

    @Test
    @Order(4)
    public void listObj() {
        // 查询主键
        String sql = "SELECT id, NAME FROM sys_region WHERE parent = :parent";
        // 入参obj
        RegionPojo pojo = new RegionPojo().setParent(-1);
        // 入参map
        Map<String, Object> params = BeanUtil.beanToMap(pojo);
        // 输出List<Map>
        List<Map<String, Object>> result11 = JdbcTemplatePlus.queryForList(sql, pojo);
        List<Map<String, Object>> result12 = JdbcTemplatePlus.queryForList(sql, params);
        assertEquals(result11.size(), result12.size());
        // 输出List<Obj>
        List<RegionPojo> result21 = JdbcTemplatePlus.queryForList(sql, pojo, RegionPojo.class);
        List<RegionPojo> result22 = JdbcTemplatePlus.queryForList(sql, params, RegionPojo.class);
        assertEquals(result21.size(), result22.size());
        // 验证
        assertEquals(result11.get(0).get(RegionPojo.FIELDS.name), result22.get(0).getName());
    }

    @Test
    @Order(5)
    public void insert() {
        // 删除测试数据
        JdbcTemplatePlus.update("DELETE FROM sys_region WHERE id in (99981,99982,99991,99992)");
        // 插入数据
        String sql = "INSERT IGNORE INTO sys_region (`id`,`name`,`code`,`parent`) VALUES (:id,:name,:code,:parent)";
        // 入参obj
        RegionPojo pojo = new RegionPojo().setId(99981).setName("测试").setCode(0).setParent(-1);
        // 入参map
        Map<String, Object> params = BeanUtil.beanToMap(pojo);
        params.put("id", 99991);
        // 单个添加
        assertEquals(JdbcTemplatePlus.update(sql, pojo), 1);
        assertEquals(JdbcTemplatePlus.update(sql, params), 1);
        // 批量添加
        pojo.setId(99982);
        params.put("id", 99992);
        assertEquals(JdbcTemplatePlus.batchUpdate(sql, Lists.newArrayList(pojo, params)), 2);
    }

    @Test
    @Order(6)
    public void update() {
        // 更新数据
        String sql = "UPDATE sys_region SET code = :code WHERE id = :id";
        // 入参obj
        RegionPojo pojo = new RegionPojo().setId(99981).setCode(1);
        // 入参map
        Map<String, Object> params = BeanUtil.beanToMap(pojo);
        params.put("id", 99991);
        // 单个添加
        assertEquals(JdbcTemplatePlus.update(sql, pojo), 1);
        assertEquals(JdbcTemplatePlus.update(sql, params), 1);
        // 批量添加
        pojo.setId(99982);
        params.put("id", 99992);
        assertEquals(JdbcTemplatePlus.batchUpdate(sql, Lists.newArrayList(pojo, params)), 2);
    }

    @Test
    @Order(7)
    public void delete() {
        assertEquals(JdbcTemplatePlus.update("DELETE FROM sys_region WHERE id in (99981,99982,99991,99992)"), 4);
    }
}
