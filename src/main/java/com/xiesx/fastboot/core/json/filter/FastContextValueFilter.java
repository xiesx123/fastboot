package com.xiesx.fastboot.core.json.filter;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.*;

import com.alibaba.fastjson2.filter.BeanContext;
import com.alibaba.fastjson2.filter.ContextValueFilter;
import com.xiesx.fastboot.core.json.annotation.GoDesensitized;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class FastContextValueFilter implements ContextValueFilter {

    private boolean desensitize = false;

    @Override
    public Object process(BeanContext context, Object object, String name, Object value) {
        if (ObjectUtil.isNotNull(value) && value instanceof String && desensitize) {
            Field field = ClassUtil.getDeclaredField(object.getClass(), name);
            GoDesensitized desensitization =
                    AnnotationUtil.getAnnotation(field, GoDesensitized.class);
            if (desensitization == null) {
                return value;
            }
            return DesensitizedUtil.desensitized((String) value, desensitization.type());
        }
        if (ObjectUtil.isNotNull(value) && value instanceof BigDecimal) {
            String format = StrUtil.nullToDefault(context.getFormat(), "0.00");
            return NumberUtil.decimalFormat(format, value);
        }
        return value;
    }
}
