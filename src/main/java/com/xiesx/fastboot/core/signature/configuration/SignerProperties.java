package com.xiesx.fastboot.core.signature.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @title SignerProperties.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:04:26
 */
@Data
@ConfigurationProperties(prefix = SignerProperties.PREFIX)
public class SignerProperties {

    public static final String PREFIX = "fastboot.signer";

    private String header = "sign";

    private String secret = "fastboot!@#";
}
