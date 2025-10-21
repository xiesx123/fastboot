package com.xiesx.fastboot.support.actuator.node;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NodeEnum {
  HTTP("http");

  private String type;

  public boolean isHttp() {
    return EnumUtil.isEnum(HTTP);
  }
}
