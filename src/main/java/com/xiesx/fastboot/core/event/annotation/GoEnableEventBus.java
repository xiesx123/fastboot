package com.xiesx.fastboot.core.event.annotation;

import com.xiesx.fastboot.core.event.configuration.EventBusCfg;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EventBusCfg.class)
@Documented
public @interface GoEnableEventBus {}
