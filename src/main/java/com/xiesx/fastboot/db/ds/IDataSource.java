package com.xiesx.fastboot.db.ds;

import javax.sql.DataSource;

/**
 * @title IDataSource.java
 * @description
 * @author Sixian.xie
 * @date 2021年3月29日 下午6:04:31
 */
public interface IDataSource {

    /**
     * 初始连接
     *
     * @param url
     * @param user
     * @param pass
     * @return
     */
    public DataSource init(String url, String user, String pass);
}
