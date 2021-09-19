package com.xiesx.fastboot.core.token.cfg;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @title TokenProperties.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:10:25
 */
@Data
@ConfigurationProperties(prefix = TokenProperties.PREFIX)
public class TokenProperties {

    public static final String PREFIX = "fastboot.token";

    private String header = "token";

    private String[] includePaths = {};

    private String[] excludePaths = {};
}
