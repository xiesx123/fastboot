package com.xiesx.fastboot.core.limiter.cfg;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @title LimiterProperties.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:01:40
 */
@Data
@ConfigurationProperties(prefix = LimiterProperties.PREFIX)
public class LimiterProperties {

    public static final String PREFIX = "fastboot.limiter";

    private Integer timeout = 1000;
}
