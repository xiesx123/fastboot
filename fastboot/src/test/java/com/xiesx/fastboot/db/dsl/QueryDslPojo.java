package com.xiesx.fastboot.db.dsl;

import lombok.AllArgsConstructor;
import lombok.Data;

public class QueryDslPojo {

  @Data
  @AllArgsConstructor
  public static class LogRecordPojo {

    public String type;

    public Long min;

    public Long max;
  }
}
