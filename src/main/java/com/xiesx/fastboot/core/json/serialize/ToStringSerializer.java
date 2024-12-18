package com.xiesx.fastboot.core.json.serialize;

import java.lang.reflect.Type;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;

import cn.hutool.core.util.StrUtil;

/**
 * @title ToStringSerializer.java
 * @description
 * @author xiesx
 * @date 2022-09-24 00:22:24
 */
public class ToStringSerializer implements ObjectWriter<Object> {

    public static final ToStringSerializer instance = new ToStringSerializer();

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        jsonWriter.writeString(StrUtil.nullToDefault(object.toString(), StrUtil.EMPTY));
    }
}
