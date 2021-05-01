package com.xiesx.fastboot.core.logger.annotation;

import java.lang.annotation.*;

import com.xiesx.fastboot.core.logger.storage.LogStorage;
import com.xiesx.fastboot.core.logger.storage.LogStorageProvider;

/**
 * @title GoLogger.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:02:45
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GoLogger {

    boolean print() default true;

    boolean format() default false;

    String operation() default "";

    Class<? extends LogStorage> storage() default LogStorageProvider.class;
}
