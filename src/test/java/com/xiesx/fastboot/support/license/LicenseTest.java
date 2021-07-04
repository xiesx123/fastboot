package com.xiesx.fastboot.support.license;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.support.license.cfg.LicenseProperties;

import cn.hutool.core.date.DateUtil;
import lombok.extern.log4j.Log4j2;

/**
 * @title LicenseTest.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:14
 */
@Log4j2
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class)
public class LicenseTest {

    @Autowired
    LicenseProperties mLicenseProperties;

    // 私钥路径
    public static String privateStorePath = "E:/license/privateKeys.store";

    // 私钥别称
    private static String publicAlias = "privateKey";

    // 私钥密码
    public static String privatepass = "xiesx@888";

    @BeforeEach
    public void creator() {
        // 生效时间
        Date issusedDate = new Date();
        log.info("license generate issusedDate {}", DateUtil.formatDateTime(issusedDate));
        // 过期时间
        Date expiryDate = DateUtil.offsetDay(issusedDate, 30);
        // expiryDate = DateUtil.parse("2021-05-01 00:00:00");
        log.info("license generate expiryDate {}", DateUtil.formatDateTime(expiryDate));
        // 额外信息
        LicenseParamsExtra licenseExtraModel = new LicenseParamsExtra();
        // 构造参数
        LicenseParams params = new LicenseParams()
                // 证书主题
                .setSubject(mLicenseProperties.getSubject())
                // 密钥库密码
                .setStorePass(mLicenseProperties.getStorePass())
                // 私钥路径
                .setPrivateKeysStorePath(privateStorePath)
                // 私钥别称
                .setPrivateAlias(publicAlias)
                // 私钥密码
                .setPrivatePass(privatepass)
                // 证书生成路径
                .setLicensePath(mLicenseProperties.getLicensePath())
                // 证书生效时间
                .setIssuedTime(issusedDate)
                // 证书失效时间
                .setExpiryTime(expiryDate)
                // 额外信息
                .setLicenseExtraModel(licenseExtraModel);
        // 生成证书
        LicenseCreator licenseCreator = new LicenseCreator(params);
        log.info("license generate {}", licenseCreator.generateLicense() ? "success" : "fail");
    }

    @Test
    @Order(1)
    public void verify() throws Exception {
        // 证书信息
        LicenseVerify param = new LicenseVerify()
                // 证书主题
                .setSubject(mLicenseProperties.getSubject())
                // 公钥别称
                .setPublicAlias(mLicenseProperties.getPublicAlias())
                // 公钥路径
                .setPublicKeysStorePath(mLicenseProperties.getPublicStorePath())
                // 密钥库密码
                .setStorePass(mLicenseProperties.getStorePass())
                // 证书路径
                .setLicensePath(mLicenseProperties.getLicensePath());
        // 安装证书
        param.install();
        // 校验证书
        param.verify();
    }
}
