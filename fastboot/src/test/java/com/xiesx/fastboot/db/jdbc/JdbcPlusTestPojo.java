package com.xiesx.fastboot.db.jdbc;

import lombok.Data;
import lombok.NoArgsConstructor;

public class JdbcPlusTestPojo {

  @Data
  @NoArgsConstructor
  public static class LogRecordPojo {

    public Long id;

    public String ip;

    public String type;

    public Long time;
  }

  @Data
  public static class CommonPojo {

    public Long ct;
  }
}
