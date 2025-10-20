package com.xiesx.fastboot.support.actuator.plans;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanEnum {
  HTTP("http");

  private String type;

  @Override
  public String toString() {
    return type;
  }

  /** 是否http */
  public boolean isHttp() {
    return EnumUtil.isEnum(HTTP);
  }
}
