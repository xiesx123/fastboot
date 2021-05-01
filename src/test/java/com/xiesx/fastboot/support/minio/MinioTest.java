package com.xiesx.fastboot.support.minio;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.base.BaseTest;
import com.xiesx.fastboot.base.config.Configed;
import com.xiesx.fastboot.support.minio.client.MinioBucketClient;
import com.xiesx.fastboot.support.minio.client.MinioObjectClient;

import io.minio.messages.Bucket;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class MinioTest extends BaseTest {

    @Autowired
    MinioBucketClient mBucketClient;

    @Autowired
    MinioObjectClient mObjectClient;

    String bktString = Configed.FASTBOOT;

    String objString = "test/test.jpg";

    @Test
    public void bucket() throws Exception {
        //
        assertEquals(mBucketClient, MinioHelper.getBucket());
        // 删除文件
        mObjectClient.deleteObject(objString);
        // 删除默认"fastboot"
        assertTrue(mBucketClient.deleteBucket());
        // 不存在
        assertEquals(mBucketClient.bucketExists(), true);
        // 创建默认"fastboot"
        assertTrue(mBucketClient.makeBucket());
        // 已存在
        assertEquals(mBucketClient.bucketExists(), true);
        // 只有1个
        assertFalse(mBucketClient.listBucketNames().isEmpty());
        assertFalse(mBucketClient.listBuckets().isEmpty());
        // 是否有内容
        assertTrue(mBucketClient.listObjects().isEmpty());
        // 获取内容
        List<Bucket> list = mBucketClient.listBuckets();
        for (Bucket bucket : list) {
            log.debug("bucket {}", bucket.name());
        }
    }

    @Test
    public void object() throws Exception {
        //
        assertEquals(mObjectClient, MinioHelper.getObject());
        // 上传文件
        assertFalse(mObjectClient.putObject(objString, "E:\\demo.jpg").bucket().isEmpty());
        //
        log.debug("object {}", mObjectClient.statObject(objString));
        log.debug("object {}", mObjectClient.userMetadata(objString));
        //
        log.debug("object {}", mObjectClient.getObjectUrl(objString));
        log.debug("object {}", mObjectClient.getObjectUrl(bktString, objString, 3));
    }
}
