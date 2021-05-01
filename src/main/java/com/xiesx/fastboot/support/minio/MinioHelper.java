package com.xiesx.fastboot.support.minio;

import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.support.minio.client.MinioBucketClient;
import com.xiesx.fastboot.support.minio.client.MinioObjectClient;

import io.minio.MinioClient;

/**
 * @title MinioHelper.java
 * @description
 * @author xiesx
 * @date 2021-04-15 10:06:14
 */
public class MinioHelper {

    public static MinioClient getClient() {
        return SpringHelper.getBean(MinioClient.class);
    }

    public static MinioBucketClient getBucket() {
        return SpringHelper.getBean(MinioBucketClient.class);
    }

    public static MinioObjectClient getObject() {
        return SpringHelper.getBean(MinioObjectClient.class);
    }
}
