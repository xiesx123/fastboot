package com.xiesx.fastboot.db.ds;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Singleton;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;

import com.alibaba.fastjson2.JSON;
import com.xiesx.fastboot.db.ds.factory.DataSourceSimpleFactory;

import lombok.extern.log4j.Log4j2;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

@Log4j2
public class TestDataSource {

    public static final String MYSQL_URL =
            "jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8&serverTimezone=GMT%2B8";

    public static final String MYSQL_USERNAME = "root";

    public static final String MYSQL_PASSWORD = "root";

    public static DataSource init(String url, String user, String pass) throws SQLException {
        // 构造数据源
        IDataSource dsf = Singleton.get(DataSourceSimpleFactory.class);
        // 初始数据源
        return dsf.init(url, user, pass);
    }

    public static void main(String[] args) throws Exception {
        // 获取源
        DataSource ds = init(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
        // 创建库
        Db db = Db.use(ds);
        // 有效性
        if (!db.getConnection().isValid(10)) {
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
