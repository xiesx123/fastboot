package com.xiesx.fastboot.base.page;

import lombok.Data;

@Data
public class PageVo {

  public Integer page = 1;

  public Integer limit = 25;

  public Integer size = 25;

  public Integer getPage() {
    return page - 1;
  }
}
