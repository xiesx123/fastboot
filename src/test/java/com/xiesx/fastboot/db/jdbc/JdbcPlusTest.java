package com.xiesx.fastboot.db.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.log.LogRecord;
import com.xiesx.fastboot.app.log.LogRecordRepository;
import com.xiesx.fastboot.app.log.QLogRecord;
import com.xiesx.fastboot.db.jdbc.pojo.CommonPojo;
import com.xiesx.fastboot.db.jdbc.pojo.LogRecordPojo;
import com.xiesx.fastboot.db.jpa.identifier.IdWorkerGenerator;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @title JdbcPlusTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:01
 */
@Transactional
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class)
public class JdbcPlusTest {

    @Autowired
    LogRecordRepository mLogRecordRepository;

    QLogRecord ql = QLogRecord.logRecord;

    List<LogRecord> result;

    @BeforeEach
    public void befoe() {
        // 零时数据
        List<LogRecord> logRecords = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            logRecords.add(new LogRecord().setIp(StrUtil.format("127.0.{}.1", i)).setMethod("test").setType("GET").setTime(10L));
        }
        // 先删除
        mLogRecordRepository.delete(ql.id.isNotNull());
        // 再添加
        result = mLogRecordRepository.insertOrUpdate(logRecords);
    }

    @Test
    @Order(1)
    public void map() {
        // 查询总数
        String sql = "SELECT COUNT(1) AS ct FROM xx_log";
        // 输出map
        Map<String, Object> result1 = JdbcTemplatePlus.queryForMap(sql);
        // 输出obj
        CommonPojo result2 = JdbcTemplatePlus.queryForMap(sql, CommonPojo.class);
        // 验证
        assertEquals(result1.getOrDefault("ct", 0), result2.getCt());
    }

    @Test
    @Order(2)
    public void map2() {
        // 查询主键
        String sql = "SELECT id, TYPE FROM xx_log WHERE ip = :ip AND type = :type";
        // 入参obj
        LogRecordPojo pojo = new LogRecordPojo().setIp("127.0.1.1").setType("GET");
        // 入参map
        Map<String, Object> params = BeanUtil.beanToMap(pojo);
        // 输出map
        Map<String, Object> result11 = JdbcTemplatePlus.queryForMap(sql, pojo);
        Map<String, Object> result12 = JdbcTemplatePlus.queryForMap(sql, params);
        assertEquals(result11.get(LogRecordPojo.FIELDS.type), result12.get(LogRecordPojo.FIELDS.type));
        // 输出obj
        LogRecordPojo result21 = JdbcTemplatePlus.queryForMap(sql, pojo, LogRecordPojo.class);
        LogRecordPojo result22 = JdbcTemplatePlus.queryForMap(sql, params, LogRecordPojo.class);
        assertEquals(result21.getType(), result22.getType());
        // 验证
        assertEquals(result11.get(LogRecordPojo.FIELDS.type), result22.getType());
    }

    @Test
    @Order(3)
    public void list() {
        // 查询10个
        String sql = "SELECT * FROM xx_log LIMIT 10";
        // 输出List<Map>
        List<Map<String, Object>> result1 = JdbcTemplatePlus.queryForList(sql);
        // 输出List<Obj>
        List<LogRecordPojo> result2 = JdbcTemplatePlus.queryForList(sql, LogRecordPojo.class);
        // 验证
        assertEquals(result1.get(0).get(LogRecordPojo.FIELDS.type), result2.get(0).getType());
    }

    @Test
    @Order(4)
    public void list2() {
        // 查询主键
        String sql = "SELECT id, TYPE FROM xx_log WHERE type = :type";
        // 入参obj
        LogRecordPojo pojo = new LogRecordPojo().setType("GET");
        // 入参map
        Map<String, Object> params = BeanUtil.beanToMap(pojo);
        // 输出List<Map>
        List<Map<String, Object>> result11 = JdbcTemplatePlus.queryForList(sql, pojo);
        List<Map<String, Object>> result12 = JdbcTemplatePlus.queryForList(sql, params);
        assertEquals(result11.size(), result12.size());
        // 输出List<Obj>
        List<LogRecordPojo> result21 = JdbcTemplatePlus.queryForList(sql, pojo, LogRecordPojo.class);
        List<LogRecordPojo> result22 = JdbcTemplatePlus.queryForList(sql, params, LogRecordPojo.class);
        assertEquals(result21.size(), result22.size());
        // 验证
        assertEquals(result11.get(0).get(LogRecordPojo.FIELDS.type), result22.get(0).getType());
    }

    @Test
    @Order(5)
    public void insert() {
        // 插入数据
        String sql =
                "insert into `xx_log` (`id`, `create_date`, `update_date`, `ip`, `method`, `type`, `url`, `req`, `res`, `time`) values(:id,  :createDate,  now(),  :ip,  :method,  :type,  :url,  :req,  :res,  :time);";
        // 入参obj
        LogRecord lr = new LogRecord()//
                .setId(IdWorkerGenerator.nextId().toString())//
                .setIp("127.0.0.1")//
                .setCreateDate(DateUtil.date())//
                .setUpdateDate(DateUtil.date()) //
                .setMethod("test")//
                .setType("GET");
        // 入参map
        Map<String, Object> params = BeanUtil.beanToMap(lr);
        params.put("id", IdWorkerGenerator.nextId());
        params.put("time", 3L);
        // 单个添加
        assertEquals(JdbcTemplatePlus.update(sql, lr), 1);
        assertEquals(JdbcTemplatePlus.update(sql, params), 1);
        // 批量添加
        result.forEach(logRecord -> {
            logRecord.setId(IdWorkerGenerator.nextId().toString());
            logRecord.setTime(4L);
        });
        assertEquals(JdbcTemplatePlus.batchUpdate(sql, result), 10);
    }

    @Test
    @Order(6)
    public void update() {
        // 更新数据
        String sql = "UPDATE xx_log SET time = :time WHERE id = :id";
        // 入参obj
        LogRecord lr = result.get(0);
        lr.setTime(2L);
        // 入参map
        Map<String, Object> params = BeanUtil.beanToMap(result.get(1));
        params.put("time", 3L);
        // 单个更新
        assertEquals(JdbcTemplatePlus.update(sql, lr), 1);
        assertEquals(JdbcTemplatePlus.update(sql, params), 1);
        // 批量更新
        result.forEach(logRecord -> {
            logRecord.setTime(4L);
        });
        assertEquals(JdbcTemplatePlus.batchUpdate(sql, result), 10);
    }

    @Test
    @Order(7)
    public void delete() {
        String sql = "SELECT COUNT(1) AS ct FROM xx_log  WHERE type = 'GET'";
        int ct = MapUtil.getInt(JdbcTemplatePlus.queryForMap(sql), "ct");
        assertEquals(JdbcTemplatePlus.update("DELETE FROM xx_log WHERE type = 'GET'"), ct);
    }
}
