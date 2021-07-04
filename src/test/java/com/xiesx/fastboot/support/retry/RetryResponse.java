package com.xiesx.fastboot.support.retry;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.base.AbstractState;

import lombok.Data;

/**
 * @title RetryResponse.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:26
 */
@Data
public class RetryResponse implements AbstractState {

    @JSONField(ordinal = 0)
    private String requestId;

    @JSONField(ordinal = 1)
    private Integer code;

    @JSONField(ordinal = 2)
    private String msg;

    @JSONField(ordinal = 3)
    private Object data;

    @JSONField(ordinal = 4)
    private Boolean success;

    @Override
    public Boolean isSuccess() {
        return code == 0;
    }
}
