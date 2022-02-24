package com.xiesx.fastboot.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.base.result.R;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * @title RequestsResponse.java
 * @description
 * @author xiesx
 * @date 2022-02-23 16:26:33
 */
@Data
@Accessors(fluent = true)
@FieldNameConstants(innerTypeName = "FIELDS")
public class InvokeResponse implements AbstractStatus {

    @JSONField(ordinal = 1)
    private Integer code = 0;

    @JSONField(ordinal = 2)
    private String msg = "";

    @JSONField(ordinal = 3)
    private Object data = new Object();

    public InvokeResponse() {}

    /**
     * 成功
     *
     * @return
     */
    @JSONField(ordinal = 4)
    public boolean isSuccess() {
        return code == 0;
    }

    /**
     * 警告
     *
     * @return
     */
    @JSONField(ordinal = 5)
    public boolean isWarn() {
        return code > 0;
    }

    /**
     * 异常
     *
     * @return
     */
    @JSONField(ordinal = 6)
    public boolean isError() {
        return code < 0;
    }

    public String toJsonString() {
        return R.toJsonStr(this);
    }

    public String toJsonPrettyStr() {
        return R.toJsonPrettyStr(this);
    }
}
