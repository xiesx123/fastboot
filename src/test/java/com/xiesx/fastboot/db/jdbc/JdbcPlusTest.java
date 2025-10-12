package com.xiesx.fastboot.db.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.log.LogRecord;
import com.xiesx.fastboot.app.log.LogRecordRepository;
import com.xiesx.fastboot.db.jdbc.JdbcPlusPojo.CommonPojo;
import com.xiesx.fastboot.db.jdbc.JdbcPlusPojo.LogRecordPojo;
import com.xiesx.fastboot.db.jpa.identifier.IdWorkerGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class)
public class JdbcPlusTest {

    @Autowired LogRecordRepository mLogRecordRepository;

    List<LogRecord> result;

    @BeforeEach
    public void befoe() {
        // 先删除
        mLogRecordRepository.deleteAll();
        // 再添加
        List<LogRecord> logRecords = Lists.newArrayList();
        for (int i = 0; i < 13; i++) {
            logRecords.add(
                    new LogRecord()
                            .setIp(StrUtil.format("127.0.{}.1", i))
                            .setMethod("test")
                            .setType(i % 2 == 0 ? "POST" : "GET")
                            .setTime(i * 10L));
        }
        result = mLogRecordRepository.insertOrUpdate(logRecords);
    }

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

    @Test
    @Order(3)
    public void list() {
        String sql = "SELECT * FROM xx_log LIMIT 10";
        // list map
        List<Map<String, Object>> r1 = JdbcTemplatePlus.queryForList(sql);
        List<LogRecordPojo> r2 = JdbcTemplatePlus.queryForList(sql, LogRecordPojo.class);
        // list bean
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
        assertEquals(JdbcTemplatePlus.batchUpdate(sql, result), 13);
    }

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
        assertEquals(JdbcTemplatePlus.batchUpdate(sql, result), 13);
    }

    @Test
    @Order(7)
    public void delete() {
        String sql = "SELECT COUNT(1) AS ct FROM xx_log  WHERE type = 'GET'";
        int ct = MapUtil.getInt(JdbcTemplatePlus.queryForMap(sql), "ct");
        assertEquals(JdbcTemplatePlus.update("DELETE FROM xx_log WHERE type = 'GET'"), ct);
    }
}
