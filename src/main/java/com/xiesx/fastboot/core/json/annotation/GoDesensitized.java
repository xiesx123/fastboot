package com.xiesx.fastboot.core.json.annotation;

import cn.hutool.core.util.DesensitizedUtil.DesensitizedType;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface GoDesensitized {

    DesensitizedType type();
}
