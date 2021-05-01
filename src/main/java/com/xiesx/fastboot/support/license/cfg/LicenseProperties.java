package com.xiesx.fastboot.support.license.cfg;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @title LicenseProperties.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:11:07
 */
@Data
@ConfigurationProperties(prefix = LicenseProperties.PREFIX)
public class LicenseProperties {

    public static final String PREFIX = "fastboot.license";

    /**
     * 证书subject
     */
    private String subject;

    /**
     * 公钥别称
     */
    private String publicAlias;

    /**
     * 密钥库密码
     */
    private String storePass;

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 公钥路径
     */
    private String publicStorePath;

}
