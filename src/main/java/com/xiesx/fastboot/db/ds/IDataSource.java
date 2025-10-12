package com.xiesx.fastboot.db.ds;

import javax.sql.DataSource;

public interface IDataSource {

    /** 初始连接 */
    public DataSource init(String url, String user, String pass);
}
