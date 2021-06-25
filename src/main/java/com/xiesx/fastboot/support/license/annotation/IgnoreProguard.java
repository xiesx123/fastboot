package com.xiesx.fastboot.support.license.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @title IgnoreProguard.java
 * @description {@link proguard} http://www.trinea.cn" target="_blank">Trinea
 * @author xiesx
 * @date 2021-06-25 01:09:18
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
public @interface IgnoreProguard {
}
