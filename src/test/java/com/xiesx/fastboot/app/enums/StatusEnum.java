package com.xiesx.fastboot.app.enums;

/**
 * @title StatusEnum.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:18:59
 */
public enum StatusEnum {

    A(0, "状态A"), B(1, "状态B"), C(2, "状态C");

    public final int index;

    public final String status;

    StatusEnum(int idx, String status) {
        this.index = idx;
        this.status = status;
    }

    public int getIndex() {
        return index;
    }

    public String getStatus() {
        return status;
    }
}
