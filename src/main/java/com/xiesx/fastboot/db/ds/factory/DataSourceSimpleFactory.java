package com.xiesx.fastboot.db.ds.factory;

import cn.hutool.db.ds.simple.SimpleDataSource;

import com.xiesx.fastboot.db.ds.IDataSource;

import javax.sql.DataSource;

public class DataSourceSimpleFactory implements IDataSource {

    @Override
    public DataSource init(String url, String user, String pass) {
        return new SimpleDataSource(url, user, pass);
    }
}
