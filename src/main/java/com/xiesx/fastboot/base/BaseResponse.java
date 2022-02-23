package com.xiesx.fastboot.base;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @title RequestsResponse.java
 * @description
 * @author xiesx
 * @date 2022-02-23 16:26:33
 */
@Data
public class BaseResponse implements AbstractStatus {

    @JSONField(ordinal = 1)
    private Integer code;

    @JSONField(ordinal = 2)
    private String msg = "";

    @JSONField(ordinal = 3)
    private Object data = new Object();

    @JSONField(ordinal = 4)
    public boolean isSuccess() {
        return code == 0;
    }
}
