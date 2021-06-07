package com.xiesx.fastboot.support.minio;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;

import cn.hutool.core.exceptions.ExceptionUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.log4j.Log4j2;

/**
 * @title MinioObjectClient.java
 * @description
 * @author xiesx
 * @date 2021-04-15 10:49:35
 */
@Log4j2
public class MinioObjectClient {

    public static final int DEFAULT_EXPIRY_TIME = (int) TimeUnit.DAYS.toSeconds(7);

    private MinioClient mClient;

    private String mDefaultBucket;

    public MinioObjectClient(MinioClient minioClient) {
        this.mClient = minioClient;
    }

    public MinioObjectClient(MinioClient minioClient, String defaultBucket) {
        this.mClient = minioClient;
        this.mDefaultBucket = defaultBucket;
    }

    // ===========

    /**
     * 文件上传
     * 
     * @param objectName
     * @param filePath
     * @return
     * @throws Exception
     */
    public ObjectWriteResponse putObject(String objectName, String filePath) throws Exception {
        validateBucketName(mDefaultBucket);
        return putObject(mDefaultBucket, null, objectName, filePath);
    }

    public ObjectWriteResponse putObject(String bucketName, String objectName, String filePath) throws Exception {
        return putObject(bucketName, null, objectName, filePath);
    }

    public ObjectWriteResponse putObject(String bucketName, String region, String objectName, String filePath) throws Exception {
        return mClient.uploadObject(UploadObjectArgs.builder().bucket(bucketName).region(region).object(objectName).filename(filePath).build());
    }


    public ObjectWriteResponse putObject(MultipartFile multipartFile) {
        validateBucketName(mDefaultBucket);
        return putObject(mDefaultBucket, null, multipartFile.getOriginalFilename(), multipartFile);
    }

    public ObjectWriteResponse putObject(String bucketName, MultipartFile multipartFile) {
        return putObject(bucketName, null, multipartFile.getOriginalFilename(), multipartFile);
    }

    public ObjectWriteResponse putObject(String bucketName, String objectName, MultipartFile multipartFile) {
        return putObject(bucketName, null, objectName, multipartFile);
    }

    public ObjectWriteResponse putObject(String bucketName, String region, String objectName, MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return mClient.putObject(PutObjectArgs.builder().bucket(bucketName).region(region).object(objectName).stream(inputStream, multipartFile.getSize(), ObjectWriteArgs.MIN_MULTIPART_SIZE)
                    .contentType(multipartFile.getContentType()).build());
        } catch (Exception e) {
            log.error("putObject {}", ExceptionUtil.getMessage(e));
        }
        return null;
    }


    public ObjectWriteResponse putObject(String objectName, InputStream stream) {
        validateBucketName(mDefaultBucket);
        return putObject(mDefaultBucket, null, objectName, stream, "application/octet-stream");
    }

    public ObjectWriteResponse putObject(String objectName, InputStream stream, String contentType) {
        validateBucketName(mDefaultBucket);
        return putObject(mDefaultBucket, null, objectName, stream, contentType);
    }

    public ObjectWriteResponse putObject(String bucketName, String objectName, InputStream stream) {
        return putObject(bucketName, null, objectName, stream, "application/octet-stream");
    }

    public ObjectWriteResponse putObject(String bucketName, String objectName, InputStream stream, String contentType) {
        return putObject(bucketName, null, objectName, stream, contentType);
    }

    public ObjectWriteResponse putObject(String bucketName, String region, String objectName, InputStream stream) {
        return putObject(bucketName, region, objectName, stream, "application/octet-stream");
    }

    public ObjectWriteResponse putObject(String bucketName, String region, String objectName, InputStream stream, String contentType) {
        try {
            ObjectWriteResponse objectWriteResponse =
                    mClient.putObject(PutObjectArgs.builder().bucket(bucketName).region(region).object(objectName).stream(stream, stream.available(), ObjectWriteArgs.MIN_MULTIPART_SIZE).contentType(contentType).build());
            return objectWriteResponse;
        } catch (Exception e) {
            log.error("putObject {}", ExceptionUtil.getMessage(e));
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                log.error("putObject {}", ExceptionUtil.getMessage(e));
            }
        }
        return null;
    }

    /**
     * 文件下载-流
     * 
     * @param objectName
     * @return
     * @throws Exception
     */
    public GetObjectResponse getObject(String objectName) throws Exception {
        validateBucketName(mDefaultBucket);
        return getObject(mDefaultBucket, null, objectName, null, null);
    }

    public GetObjectResponse getObject(String objectName, Long offset, Long length) throws Exception {
        validateBucketName(mDefaultBucket);
        return getObject(mDefaultBucket, null, objectName, offset, length);
    }

    public GetObjectResponse getObject(String bucketName, String objectName) throws Exception {
        return getObject(bucketName, null, objectName, null, null);
    }

    public GetObjectResponse getObject(String bucketName, String region, String objectName) throws Exception {
        return getObject(bucketName, region, objectName, null, null);
    }

    public GetObjectResponse getObject(String bucketName, String objectName, Long offset, Long length) throws Exception {
        return getObject(bucketName, null, objectName, offset, length);
    }

    public GetObjectResponse getObject(String bucketName, String region, String objectName, Long offset, Long length) throws Exception {
        return mClient.getObject(GetObjectArgs.builder().bucket(bucketName).region(region).object(objectName).offset(offset).length(length).build());
    }

    /**
     * 文件下载-流
     * 
     * @param objectName
     * @param response
     */
    public void downloadObject(String objectName, HttpServletResponse response) {
        validateBucketName(mDefaultBucket);
        downloadObject(mDefaultBucket, null, objectName, response);
    }

    public void downloadObject(String bucketName, String objectName, HttpServletResponse response) {
        downloadObject(bucketName, null, objectName, response);
    }

    public void downloadObject(String bucketName, String region, String objectName, HttpServletResponse response) {
        // 设置编码
        response.setCharacterEncoding("UTF-8");
        try (ServletOutputStream os = response.getOutputStream(); GetObjectResponse is = mClient.getObject(GetObjectArgs.builder().bucket(bucketName).region(region).object(objectName).build());) {
            response.setHeader("Content-Disposition", "attachment;objectName=" + new String(objectName.getBytes("gb2312"), "ISO8859-1"));
            ByteStreams.copy(is, os);
            os.flush();
        } catch (Exception e) {
            log.error("downloadObject {}", ExceptionUtil.getMessage(e));
        }
    }

    public void downloadObject(String objectName, String filename) throws Exception {
        validateBucketName(mDefaultBucket);
        downloadObject(mDefaultBucket, null, objectName, filename);
    }

    public void downloadObject(String bucketName, String objectName, String filename) throws Exception {
        downloadObject(bucketName, null, objectName, filename);
    }

    public void downloadObject(String bucketName, String region, String objectName, String filename) throws Exception {
        mClient.downloadObject(DownloadObjectArgs.builder().bucket(bucketName).region(region).object(objectName).filename(filename).build());
    }

    /**
     * 获取文件-URL
     * 
     * @param objectName
     * @return
     * @throws Exception
     */
    public String getObjectUrl(String objectName) throws Exception {
        validateBucketName(mDefaultBucket);
        return getObjectUrl(mDefaultBucket, null, objectName, DEFAULT_EXPIRY_TIME, Maps.newConcurrentMap());
    }

    public String getObjectUrl(String bucketName, String objectName) throws Exception {
        return getObjectUrl(bucketName, null, objectName, DEFAULT_EXPIRY_TIME, Maps.newConcurrentMap());
    }

    public String getObjectUrl(String bucketName, String region, String objectName) throws Exception {
        return getObjectUrl(bucketName, null, objectName, DEFAULT_EXPIRY_TIME, Maps.newConcurrentMap());
    }

    public String getObjectUrl(String bucketName, String objectName, int expiry) throws Exception {
        return getObjectUrl(bucketName, null, objectName, expiry, Maps.newConcurrentMap());
    }

    public String getObjectUrl(String bucketName, String region, String objectName, Integer expiry) throws Exception {
        return getObjectUrl(bucketName, region, objectName, expiry, Maps.newConcurrentMap());
    }

    public String getObjectUrl(String bucketName, String region, String objectName, Integer expires, Map<String, String> params) throws Exception {
        validateExpiry(expires);
        return mClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).region(region).object(objectName).expiry(expires).extraQueryParams(Maps.newHashMap(params)).build());
    }

    /**
     * 根据文件前缀查询文件
     * 
     * @param prefix
     * @param recursive
     * @return
     * @throws Exception
     */
    public List<Item> getAllObjectsByPrefix(String prefix, boolean recursive) throws Exception {
        validateBucketName(mDefaultBucket);
        return getAllObjectsByPrefix(mDefaultBucket, prefix, recursive);
    }

    public List<Item> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) throws Exception {
        List<Item> list = Lists.newArrayList();
        Iterable<Result<Item>> objectsIterator = mClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive).build());
        if (objectsIterator != null) {
            Iterator<Result<Item>> iterator = objectsIterator.iterator();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    Result<Item> result = iterator.next();
                    Item item = result.get();
                    list.add(item);
                }
            }
        }
        return list;
    }

    /**
     * 获取对象的元数据
     * 
     * @param objectName
     * @return
     * @throws Exception
     */
    public Map<String, String> userMetadata(String objectName) throws Exception {
        validateBucketName(mDefaultBucket);
        return statObject(mDefaultBucket, null, objectName).userMetadata();
    }

    public Map<String, String> userMetadata(String bucketName, String objectName) throws Exception {
        return statObject(bucketName, null, objectName).userMetadata();
    }

    public Map<String, String> userMetadata(String bucketName, String region, String objectName) throws Exception {
        return statObject(bucketName, region, objectName).userMetadata();
    }

    /**
     * 统计对象(含元数据)-判断对象是否存在
     * 
     * @param objectName
     * @return
     * @throws Exception
     */
    public StatObjectResponse statObject(String objectName) throws Exception {
        validateBucketName(mDefaultBucket);
        return statObject(mDefaultBucket, null, objectName);
    }

    public StatObjectResponse statObject(String bucketName, String objectName) throws Exception {
        return statObject(bucketName, null, objectName);
    }

    public StatObjectResponse statObject(String bucketName, String region, String objectName) throws Exception {
        return mClient.statObject(StatObjectArgs.builder().bucket(bucketName).region(region).object(objectName).build());
    }

    /**
     * 删除文件-单个
     * 
     * @param objectName
     * @throws Exception
     */
    public void deleteObject(String objectName) throws Exception {
        validateBucketName(mDefaultBucket);
        deleteObject(mDefaultBucket, null, objectName);
    }

    public void deleteObject(String bucketName, String objectName) throws Exception {
        deleteObject(bucketName, null, objectName);
    }

    public void deleteObject(String bucketName, String region, String objectName) throws Exception {
        mClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).region(region).object(objectName).build());
    }

    /**
     * 删除文件-多个
     * 
     * @param objectNames
     * @throws Exception
     */
    public void deleteObjects(List<DeleteObject> objectNames) throws Exception {
        validateBucketName(mDefaultBucket);
        deleteObjects(mDefaultBucket, null, objectNames);
    }

    public void deleteObjects(String bucketName, List<DeleteObject> objectNames) throws Exception {
        deleteObjects(bucketName, null, objectNames);
    }

    public void deleteObjects(String bucketName, String region, List<DeleteObject> objectNames) throws Exception {
        for (Result<DeleteError> errorResult : mClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).region(region).objects(objectNames).build())) {
            DeleteError deleteError = errorResult.get();
            log.error("Failed to remove {}，DeleteError:", deleteError.message());
        }
    }

    /**
     * 验证到期时间
     * 
     * @param expiry
     */
    private void validateExpiry(Integer expiry) {
        if (expiry < 1 || expiry > DEFAULT_EXPIRY_TIME) {
            throw new RunException(RunExc.MINIO, "expiry must be minimum 1 second to maximum " + TimeUnit.SECONDS.toDays(DEFAULT_EXPIRY_TIME) + " days");
        }
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
