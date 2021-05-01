package com.xiesx.fastboot.support.retry;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.base.AbstractState;

import lombok.Data;

@Data
public class RetryResponse implements AbstractState {

    @JSONField(ordinal = 1)
    private Integer code;

    @JSONField(ordinal = 2)
    private String msg;

    @JSONField(ordinal = 3)
    private Object data;

    @JSONField(ordinal = 3)
    private Boolean success;

    @Override
    public Boolean isSuccess() {
        return code == 0;
    }
}
