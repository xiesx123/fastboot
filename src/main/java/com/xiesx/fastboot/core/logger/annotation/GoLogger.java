package com.xiesx.fastboot.core.logger.annotation;

import com.xiesx.fastboot.core.logger.storage.LogStorage;
import com.xiesx.fastboot.core.logger.storage.LogStorageProvider;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GoLogger {

    boolean print() default true;

    boolean format() default false;

    String operation() default "";

    Class<? extends LogStorage> storage() default LogStorageProvider.class;
}
