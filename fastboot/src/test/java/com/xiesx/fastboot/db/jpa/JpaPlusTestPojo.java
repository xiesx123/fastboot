package com.xiesx.fastboot.db.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;

public class JpaPlusTestPojo {

  @Data
  @AllArgsConstructor
  public static class LogRecordPojo {

    public String type;

    public Long min;

    public Long max;
  }
}
