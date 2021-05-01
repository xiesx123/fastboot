package com.xiesx.fastboot.support.minio.client;

import java.util.List;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.log4j.Log4j2;

/**
 * @title MinioBucketService.java
 * @description
 * @author xiesx
 * @date 2021-04-15 10:18:47
 */
@Log4j2
public class MinioBucketClient {

    private MinioClient mClient;

    private String mDefaultBucket;

    public MinioBucketClient(MinioClient minioClient) {
        this.mClient = minioClient;
        init();
    }

    public MinioBucketClient(MinioClient minioClient, String defaultBucket) {
        this.mClient = minioClient;
        this.mDefaultBucket = defaultBucket;
        init();
    }

    public void init() {
        if (ObjectUtil.isAllNotEmpty(mClient, mDefaultBucket)) {
            log.info("Minio Client init {}", makeBucket() ? "Success" : "Failed");
        }
    }

    /**
     * 判断桶是否存在
     * 
     * @return
     * @throws Exception
     */
    public boolean bucketExists() throws Exception {
        validateBucketName(mDefaultBucket);
        return bucketExists(mDefaultBucket, null);
    }

    public boolean bucketExists(String bucketName) throws Exception {
        return bucketExists(bucketName, null);
    }

    public boolean bucketExists(String bucketName, String region) throws Exception {
        return mClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).region(region).build());
    }

    /**
     * 创建桶
     * 
     * @return
     */
    public boolean makeBucket() {
        validateBucketName(mDefaultBucket);
        return makeBucket(mDefaultBucket, null);
    }

    public boolean makeBucket(String bucketName) {
        return makeBucket(bucketName, null);
    }

    public boolean makeBucket(String bucketName, String region) {
        try {
            boolean isExist = bucketExists(bucketName, region);
            if (!isExist) {
                mClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).region(region).build());
            }
            return true;
        } catch (Exception e) {
            log.error("makeBucket {}", ExceptionUtil.getMessage(e));
            return false;
        }
    }

    /**
     * 列出所有存储桶
     * 
     * @return
     * @throws Exception
     */
    public List<Bucket> listBuckets() throws Exception {
        return mClient.listBuckets();
    }

    /**
     * 列出所有存储桶 （名称）
     * 
     * @return
     * @throws Exception
     */
    public List<String> listBucketNames() throws Exception {
        List<Bucket> bucketList = listBuckets();
        List<String> bucketListName = Lists.newArrayList();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 列出默认桶中所有对象
     * 
     * @return
     * @throws Exception
     */
    public List<Result<Item>> listObjects() throws Exception {
        validateBucketName(mDefaultBucket);
        return listObjects(mDefaultBucket);
    }

    public List<Result<Item>> listObjects(String bucketName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return Lists.newArrayList(mClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build()));
        }
        return null;
    }

    /**
     * 列出存储桶中的所有对象（名称）
     * 
     * @param bucketName
     * @return
     * @throws Exception
     */
    public List<String> listObjectNames(String bucketName) throws Exception {
        List<String> listObjectNames = Lists.newArrayList();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            List<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    /**
     * 获取默认存储桶策略
     * 
     * @throws Exception
     */
    public void getBucketPolicy() throws Exception {
        validateBucketName(mDefaultBucket);
        mClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(mDefaultBucket).build());
    }

    /**
     * 设置默认存储桶策略
     * 
     * @throws Exception
     */
    public void setBucketPolicy(String policyJson) throws Exception {
        validateBucketName(mDefaultBucket);
        setBucketPolicy(mDefaultBucket, null, policyJson);
    }

    public void getBucketPolicy(String bucketName) throws Exception {
        setBucketPolicy(bucketName, null, null);
    }

    public void getBucketPolicy(String bucketName, String region) throws Exception {
        setBucketPolicy(bucketName, region, null);
    }

    public void setBucketPolicy(String bucketName, String policyJson) throws Exception {
        setBucketPolicy(bucketName, null, policyJson);
    }

    public void setBucketPolicy(String bucketName, String region, String policyJson) throws Exception {
        mClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).region(region).config(policyJson).build());
    }

    /**
     * 删除桶
     * 
     * @return
     */
    public boolean deleteBucket() {
        validateBucketName(mDefaultBucket);
        return deleteBucket(mDefaultBucket, null);
    }

    public boolean deleteBucket(String bucketName) {
        return deleteBucket(bucketName, null);
    }

    public boolean deleteBucket(String bucketName, String region) {
        try {
            mClient.deleteBucketEncryption(DeleteBucketEncryptionArgs.builder().bucket(bucketName).region(region).build());
            return true;
        } catch (Exception e) {
            log.error("makeBucket {}", ExceptionUtil.getMessage(e));
            return false;
        }
    }

    public boolean deleteBucketByNull(String bucketName) throws Exception {
        return deleteBucketByNull(bucketName, null);
    }

    public boolean deleteBucketByNull(String bucketName, String region) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                // 有对象文件，则删除失败
                if (item.size() > 0) {
                    return false;
                }
            }
            // 删除存储桶，注意，只有存储桶为空时才能删除成功。
            mClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).region(region).build());
            flag = bucketExists(bucketName);
            if (!flag) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证桶
     * 
     * @param name
     */
    protected void validateBucketName(String name) {
        // 验证桶
        validateNotNull(name, "bucket name");
        // 判断
        if (mClient == null) {
            throw new RunException(RunExc.MINIO, name + " : " + "client must be init");
        }
        // Bucket names cannot be no less than 3 and no more than 63 characters long.
        if (name.length() < 3 || name.length() > 63) {
            throw new RunException(RunExc.MINIO, name + " : " + "bucket name must be at least 3 and no more than 63 characters long");
        }
        // Successive periods in bucket names are not allowed.
        if (name.contains("..")) {
            String msg = "bucket name cannot contain successive periods. For more information refer " + "http://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html";
            throw new RunException(RunExc.MINIO, name + " : " + msg);
        }
        // Bucket names should be dns compatible.
        if (!name.matches("^[a-z0-9][a-z0-9\\.\\-]+[a-z0-9]$")) {
            String msg = "bucket name does not follow Amazon S3 standards. For more information refer " + "http://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html";
            throw new RunException(RunExc.MINIO, name + " : " + msg);
        }
    }

    /**
     * 验证非空
     * 
     * @param arg
     * @param argName
     */
    protected void validateNotNull(Object arg, String argName) {
        if (arg == null) {
            throw new RunException(RunExc.MINIO, argName + " must not be null,Must be configured dist.minio.config.default-bucket=");
        }
    }
}
