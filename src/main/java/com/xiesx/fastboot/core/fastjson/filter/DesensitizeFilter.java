package com.xiesx.fastboot.core.fastjson.filter;

import java.lang.reflect.Field;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.xiesx.fastboot.core.fastjson.annotation.GoDesensitized;

import cn.hutool.core.util.DesensitizedUtil;

/**
 * @title DesensitizeFilter.java
 * @description
 * @author xiesx
 * @date 2021-04-04 17:59:53
 */
public class DesensitizeFilter implements ValueFilter {

    @Override
    public Object process(Object object, String name, Object value) {
        if (null == value || !(value instanceof String) || ((String) value).length() == 0) {
            return value;
        }
        GoDesensitized desensitization = null;
        try {
            Field field = object.getClass().getDeclaredField(name);
            if (String.class != field.getType() || (desensitization = field.getAnnotation(GoDesensitized.class)) == null) {
                return value;
            }
            return DesensitizedUtil.desensitized((String) value, desensitization.type());
        } catch (NoSuchFieldException | SecurityException e) {
            return value;
        }
    }
}

