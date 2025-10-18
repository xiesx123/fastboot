package com.xiesx.fastboot.db.jpa.identifier;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IdGeneratorType(GeneratorIdWorker.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedIdId {

    String prefix() default "";

    int centerId() default 0;

    int workerId() default 0;
}
