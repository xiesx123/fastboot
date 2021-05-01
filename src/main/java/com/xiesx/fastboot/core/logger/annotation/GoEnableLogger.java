package com.xiesx.fastboot.core.logger.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import com.xiesx.fastboot.core.logger.cfg.LoggerCfg;

/**
 * @title GoEnableLogger.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:02:41
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({LoggerCfg.class})
@Documented
public @interface GoEnableLogger {
}
