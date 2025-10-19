package com.xiesx.fastboot.core.json.serialize;

import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;

import java.lang.reflect.Type;

public class ToDateReader implements ObjectReader<Object> {

    public static final ToDateReader instance = new ToDateReader();

    @Override
    public Object readObject(
            JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        return DateUtil.parse(jsonReader.readString());
    }
}
