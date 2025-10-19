package com.xiesx.fastboot.core.token.configuration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = TokenProperties.PREFIX)
public class TokenProperties {

    public static final String PREFIX = "fastboot.token";

    public static final String SECRET = "fastboot!@#";

    private String header = "token";

    private String secret = SECRET;

    private String[] includePaths = {};

    private String[] excludePaths = {};
}
