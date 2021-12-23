package com.xiesx.fastboot.core.eventbus.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import com.xiesx.fastboot.core.eventbus.configuration.EventBusCfg;

/**
 * @title GoEnableEventBus.java
 * @description
 * @author xiesx
 * @date 2021-04-24 01:34:54
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EventBusCfg.class)
@Documented
public @interface GoEnableEventBus {
}
