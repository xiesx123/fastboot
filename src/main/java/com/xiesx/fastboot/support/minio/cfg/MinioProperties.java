package com.xiesx.fastboot.support.minio.cfg;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.xiesx.fastboot.base.config.Configed;

import lombok.Data;

/**
 * @title MinioProperties.java
 * @description
 * @author xiesx
 * @date 2021-04-15 09:58:07
 */
@Data
@ConfigurationProperties(prefix = MinioProperties.PREFIX)
public class MinioProperties {

    public static final String PREFIX = "fastboot.minio";

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 服务地址
     */
    private String address;

    /**
     * 服务访问密钥
     */
    private String accessKey;

    /**
     * 服务秘密密钥
     */
    private String secretKey;

    /**
     * 默认桶
     */
    private String bucket = Configed.FASTBOOT;

    /**
     * 是否启用https
     */
    private boolean secure = false;
}
