package com.xiesx.fastboot.db.jpa.identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.annotations.IdGeneratorType;

/**
 * @title GeneratedIdWorker.java
 * @description
 * @author xiesx
 * @date 2024-12-19 03:13:24
 */
@IdGeneratorType(IdWorkerGenerator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedIdWorker {

    String prefix() default "";

    int workerId() default 1;

    int centerId() default 50;
}
