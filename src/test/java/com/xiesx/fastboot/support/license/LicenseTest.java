package com.xiesx.fastboot.support.license;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.xiesx.fastboot.base.config.Configed;

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
public class LicenseTest {

    // 主题
    public static String subject = Configed.FASTBOOT;

    // 密钥库密码
    public static String storepass = "136305973@qq.com";

    // 私钥密码
    public static String privatepass = "xiesx@888";

    // 私钥路径
    public static String privateStorePath = "F:/license/privateKeys.store";

    // 公钥路径
    public static String publicStorePath = "F:/license/publicCerts.store";

    // 证书
    public static String licensePath = "F:/license/license.lic";

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
                .setSubject(subject)
                // 密钥库密码
                .setStorePass(storepass)
                // 私钥路径
                .setPrivateKeysStorePath(privateStorePath)
                // 私钥别称
                .setPrivateAlias("privateKey")
                // 私钥密码
                .setPrivatePass(privatepass)
                // 证书生成路径
                .setLicensePath(licensePath)
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
                .setSubject(subject)
                // 公钥别称
                .setPublicAlias("publicCert")
                // 公钥路径
                .setPublicKeysStorePath(publicStorePath)
                // 密钥库密码
                .setStorePass(storepass)
                // 证书路径
                .setLicensePath(licensePath);
        // 安装证书
        param.install();
        // 校验证书
        param.verify();
    }
}
