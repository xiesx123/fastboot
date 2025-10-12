package com.xiesx.fastboot.core.json.serialize;

import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;

import java.lang.reflect.Type;

public class ToStringWriter implements ObjectWriter<Object> {

    public static final ToStringWriter instance = new ToStringWriter();

    @Override
    public void write(
            JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {

        jsonWriter.writeString(StrUtil.nullToDefault(object.toString(), StrUtil.EMPTY));
    }
}
