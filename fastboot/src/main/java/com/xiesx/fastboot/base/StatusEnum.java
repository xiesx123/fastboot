package com.xiesx.fastboot.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
  SUCCESS(0, "success"),

  FAIL(-1, "fail"),

  ERROR(-2, "error"),

  RETRY(-3, "retry");

  private Integer code;

  private String msg;
}
