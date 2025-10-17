package com.xiesx.fastboot.core.token.configuration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = TokenProperties.PREFIX)
public class TokenProperties {

    public static final String PREFIX = "fastboot.token";

    private String header = "token";

    private String[] includePaths = {};

    private String[] excludePaths = {};
}
