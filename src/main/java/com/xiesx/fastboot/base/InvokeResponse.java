package com.xiesx.fastboot.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.base.result.Result;

import lombok.experimental.Accessors;

/**
 * @title InvokeResponse.java
 * @description
 * @author xiesx
 * @date 2022-02-23 16:26:33
 */
@Accessors(fluent = true)
public class InvokeResponse<T> extends Result {

    /**
     * 数据
     */
    @JSONField(ordinal = 3)
    public T data;

    public T data() {
        return data;
    }
}
