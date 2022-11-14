package com.xiesx.fastboot.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @title StatusEnum.java
 * @description
 * @author xiesx
 * @date 2022-11-14 22:16:26
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    SUCCESS(0, "success"),

    FAIL(-1, "fail"),

    ERROR(-2, "error"),

    RETRY(-3, "retry");

    private Integer code;

    private String msg;

    @Override
    public String toString() {
        return msg;
    }
}
