package com.xiesx.fastboot.support.minio;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.base.config.Configed;

import cn.hutool.core.util.ObjectUtil;
import io.minio.messages.Bucket;
import lombok.extern.log4j.Log4j2;

/**
 * @title MinioTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:18
 */
@Log4j2
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class)
public class MinioTest {

    MinioBucketClient mBucketClient = SpringHelper.getBean(MinioBucketClient.class);

    MinioObjectClient mObjectClient = SpringHelper.getBean(MinioObjectClient.class);

    String bktString = Configed.FASTBOOT;

    String objString = "test/test.jpg";

    @Test
    @Order(1)
    public void bucket() throws Exception {
        if (ObjectUtil.isAllNotEmpty(mBucketClient, mObjectClient)) {
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
    }

    @Test
    @Order(2)
    public void object() throws Exception {
        if (ObjectUtil.isAllNotEmpty(mBucketClient, mObjectClient)) {
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
}
