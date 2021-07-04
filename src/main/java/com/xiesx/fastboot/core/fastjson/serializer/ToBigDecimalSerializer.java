package com.xiesx.fastboot.core.fastjson.serializer;

import com.alibaba.fastjson.serializer.*;

import cn.hutool.core.util.NumberUtil;

/**
 * @title ToBigDecimalSerializer.java
 * @description 自定义BigDecimal系列化
 * @author xiesx
 * @date 2020-4-2 10:43:03
 */
public class ToBigDecimalSerializer extends BigDecimalCodec implements ContextObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, BeanContext context) {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeString("");
            return;
        }
        out.writeString(NumberUtil.decimalFormat(context.getFormat(), object));
    }
}
