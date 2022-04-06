package com.xiesx.fastboot.db.tool;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.alibaba.fastjson.JSON;
import com.xiesx.fastboot.db.tool.factory.DataSourceSimpleFactory;

import cn.hutool.core.lang.Singleton;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import lombok.extern.log4j.Log4j2;

/**
 * @title TestDataSource.java
 * @description
 * @author Sixian.xie
 * @date 2021年3月29日 下午6:04:35
 */
@Log4j2
public class TestDataSource {

    public final static String MYSQL_URL = "jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8&serverTimezone=GMT%2B8";

    public final static String MYSQL_USERNAME = "root";

    public final static String MYSQL_PASSWORD = "root";

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
            System.out.println(JSON.toJSONString(entity));
        }
    }
}
