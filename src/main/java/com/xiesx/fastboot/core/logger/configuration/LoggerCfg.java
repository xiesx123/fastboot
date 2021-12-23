package com.xiesx.fastboot.core.logger.configuration;

import org.springframework.context.annotation.Import;

import com.xiesx.fastboot.core.logger.LoggerAspect;

/**
 * @title LoggerCfg.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:02:56
 */
@Import({LoggerAspect.class})
public class LoggerCfg {
}
