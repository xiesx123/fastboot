package com.xiesx.fastboot.db.jdbc.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * @title RegionPojo.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:58
 */
@Data
@Accessors(chain = true)
@FieldNameConstants(innerTypeName = "FIELDS")
public class LogRecordPojo {

    public Long id;

    public String ip;

    public String type;

    public Long time;
}
