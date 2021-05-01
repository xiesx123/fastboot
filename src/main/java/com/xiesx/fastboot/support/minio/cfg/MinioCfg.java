package com.xiesx.fastboot.support.minio.cfg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xiesx.fastboot.support.minio.client.MinioBucketClient;
import com.xiesx.fastboot.support.minio.client.MinioObjectClient;

import cn.hutool.core.util.StrUtil;
import io.minio.MinioClient;

/**
 * @title MinioCfg.java
 * @description
 * @author xiesx
 * @date 2021-04-15 09:57:59
 */
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
@ConditionalOnProperty(prefix = MinioProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass({MinioClient.class})
public class MinioCfg {

    @Autowired
    MinioProperties mMinioProperties;

    @Bean
    public MinioClient minioClient() {
        if (StrUtil.isNotEmpty(mMinioProperties.getPoint())) {
            MinioClient mClient = MinioClient.builder()//
                    .endpoint(mMinioProperties.getPoint(), mMinioProperties.getPort(), mMinioProperties.isSecure())//
                    .credentials(mMinioProperties.getAccessKey(), mMinioProperties.getSecretKey())//
                    .build();
            return mClient;
        }
        return null;
    }

    @Bean
    public MinioBucketClient minioBucketService(MinioClient minioClient) {
        if (StrUtil.isEmpty(mMinioProperties.getBucket())) {
            return new MinioBucketClient(minioClient);
        }
        return new MinioBucketClient(minioClient, mMinioProperties.getBucket());
    }

    @Bean
    public MinioObjectClient mMinioPropertiesObjectService(MinioClient minioClient) {
        if (StrUtil.isEmpty(mMinioProperties.getBucket())) {
            return new MinioObjectClient(minioClient);
        }
        return new MinioObjectClient(minioClient, mMinioProperties.getBucket());
    }
}
