package com.xiesx.fastboot.db.ds;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Singleton;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;

import com.alibaba.fastjson2.JSON;
import com.xiesx.fastboot.db.ds.factory.DataSourceSimpleFactory;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import javax.sql.DataSource;

@Log4j2
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class IDataSourceTest {

    public static final String MYSQL_URL =
            "jdbc:p6spy:mysql://127.0.0.1:3306/test?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai";

    public static final String MYSQL_USERNAME = "root";

    public static final String MYSQL_PASSWORD = "root";

    IDataSource dsf;

    @BeforeEach
    void setup() {
        dsf = Singleton.get(DataSourceSimpleFactory.class);
    }

    @Test
    @Order(1)
    void testConnection() throws Exception {
        DataSource ds = dsf.init(MYSQL_USERNAME, MYSQL_PASSWORD, MYSQL_URL);
        // 创建库
        Db db = Db.use(ds);
        // 有效性
        if (!db.getConnection().isValid(100)) {
            log.error("mysql invalid");
            return;
        }
        // 查询数据(sql)
        List<Entity> list = db.query("SHOW TABLES;");
        for (Entity entity : list) {
            Console.log(JSON.toJSONString(entity));
        }
    }

    @Test
    @Order(2)
    void testConnectionWithDriver() throws Exception {
        DataSource ds =
                dsf.init(
                        MYSQL_USERNAME,
                        MYSQL_PASSWORD,
                        MYSQL_URL,
                        "com.p6spy.engine.spy.P6SpyDriver");
        // 创建库
        Db db = Db.use(ds);
        // 有效性
        if (!db.getConnection().isValid(100)) {
            log.error("mysql invalid");
            return;
        }
        // 查询数据(sql)
        List<Entity> list = db.query("SHOW TABLES;");
        for (Entity entity : list) {
            Console.log(JSON.toJSONString(entity));
        }
    }
}
