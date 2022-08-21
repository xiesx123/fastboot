package com.xiesx.fastboot.core.json.filter;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextValueFilter;
import com.xiesx.fastboot.core.json.annotation.GoDesensitized;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title FastContextValueFilter.java
 * @description
 * @author xiesx
 * @date 2022-07-23 14:01:32
 */
@Data
@NoArgsConstructor
public class FastContextValueFilter implements ContextValueFilter {

    private boolean desensitize = true;

    @Override
    public Object process(BeanContext context, Object object, String name, Object value) {
        if (ObjectUtil.isNotNull(value) && value instanceof String) {
            Field field = ClassUtil.getDeclaredField(object.getClass(), name);
            GoDesensitized desensitization = AnnotationUtil.getAnnotation(field, GoDesensitized.class);
            if (desensitization == null) {
                return value;
            }
            return DesensitizedUtil.desensitized((String) value, desensitization.type());
        }
        if (ObjectUtil.isNotNull(value) && value instanceof BigDecimal) {
            return NumberUtil.decimalFormat(context.getFormat(), value);
        }
        return value;
    }
}
