package com.xiesx.fastboot.db.ds.factory;

import javax.sql.DataSource;

import com.xiesx.fastboot.db.ds.IDataSource;

import cn.hutool.db.ds.simple.SimpleDataSource;

/**
 * @title DataSourceSimpleFactory.java
 * @description
 * @author Sixian.xie
 * @date 2021年3月29日 下午6:04:27
 */
public class DataSourceSimpleFactory implements IDataSource {

    @Override
    public DataSource init(String url, String user, String pass) {
        return new SimpleDataSource(url, user, pass);
    }
}
