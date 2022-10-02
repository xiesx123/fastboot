package com.xiesx.fastboot.core.json.serialize;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import cn.hutool.core.util.StrUtil;

/**
 * @title ToStringSerializer.java
 * @description
 * @author xiesx
 * @date 2022-09-24 00:22:24
 */
public class ToStringSerializer implements ObjectSerializer {

    public static final ToStringSerializer instance = new ToStringSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        out.writeString(StrUtil.nullToDefault(object.toString(), StrUtil.EMPTY));
    }
}
