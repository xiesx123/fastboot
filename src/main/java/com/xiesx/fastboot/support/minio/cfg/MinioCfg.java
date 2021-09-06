package com.xiesx.fastboot.support.minio.cfg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xiesx.fastboot.support.minio.MinioBucketClient;
import com.xiesx.fastboot.support.minio.MinioObjectClient;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
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
        String address = mMinioProperties.getAddress();
        if (CharSequenceUtil.isNotBlank(address)) {
            String point = CharSequenceUtil.subBefore(address, ":", true);
            Integer port = Convert.toInt(CharSequenceUtil.subAfter(address, ":", true));
            MinioClient mClient = MinioClient.builder()//
                    .endpoint(point, port, mMinioProperties.isSecure())//
                    .credentials(mMinioProperties.getAccessKey(), mMinioProperties.getSecretKey())//
                    .build();
            return mClient;
        }
        return null;
    }

    @Bean
    public MinioBucketClient minioBucketService(MinioClient minioClient) {
        if (CharSequenceUtil.isEmpty(mMinioProperties.getBucket())) {
            return new MinioBucketClient(minioClient);
        }
        return new MinioBucketClient(minioClient, mMinioProperties.getBucket());
    }

    @Bean
    public MinioObjectClient mMinioPropertiesObjectService(MinioClient minioClient) {
        if (CharSequenceUtil.isEmpty(mMinioProperties.getBucket())) {
            return new MinioObjectClient(minioClient);
        }
        return new MinioObjectClient(minioClient, mMinioProperties.getBucket());
    }
}
