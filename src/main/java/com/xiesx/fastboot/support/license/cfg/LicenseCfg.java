package com.xiesx.fastboot.support.license.cfg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;

import com.xiesx.fastboot.support.license.LicenseVerify;

import cn.hutool.core.util.ObjectUtil;
import de.schlichtherle.license.LicenseCreator;
import de.schlichtherle.license.LicenseVerifier;
import lombok.extern.log4j.Log4j2;

/**
 * @title LicenseCfg.java
 * @description
 * @author xiesx
 * @date 2020-7-21 22:33:47
 */
@Log4j2
@EnableConfigurationProperties(LicenseProperties.class)
@ConditionalOnClass({LicenseCreator.class, LicenseVerifier.class})
public class LicenseCfg {

    @Autowired
    LicenseProperties mLicenseProperties;

    @PostConstruct
    public void postConstruct() {
        // 获取参数
        if (ObjectUtil.isAllNotEmpty(mLicenseProperties.getLicensePath())) {
            // 构造
            LicenseVerify param = new LicenseVerify()
                    // 证书主题
                    .setSubject(mLicenseProperties.getSubject())
                    // 公钥库存储路径
                    .setPublicKeysStorePath(mLicenseProperties.getPublicStorePath())
                    // 访问公钥库的密码
                    .setStorePass(mLicenseProperties.getStorePass())
                    // 公钥别称
                    .setPublicAlias(mLicenseProperties.getPublicAlias())
                    // 证书生成路径
                    .setLicensePath(mLicenseProperties.getLicensePath());
            try {
                // 安装证书
                param.install();
                if (!param.verify()) {
                    // System.exit(1);
                }
            } catch (Exception e) {
                log.error("Startup License Verify Error {}", e.getMessage());
                // System.exit(1);
            }
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void taskLicense() {
        postConstruct();
    }
}
