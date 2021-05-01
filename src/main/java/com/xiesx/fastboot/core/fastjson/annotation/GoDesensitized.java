package com.xiesx.fastboot.core.fastjson.annotation;

import java.lang.annotation.*;

import cn.hutool.core.util.DesensitizedUtil.DesensitizedType;

/**
 * @title GoDesensitized.java
 * @description
 * @author xiesx
 * @date 2021-04-04 17:59:21
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface GoDesensitized {

    // 脱敏类型(规则)
    DesensitizedType type();
}
