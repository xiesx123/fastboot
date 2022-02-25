package com.xiesx.fastboot.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.base.result.Result;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
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

    public static void main(String[] args) {
        String json = "{\"code\":0,\"msg\":\"ok\",\"data\":{\"id\":1642321505626}}";

        InvokeResponse<Dict> res12 = JSON.parseObject(json, new TypeReference<InvokeResponse<Dict>>() {});
        Console.log(res12.data());
    }
}
