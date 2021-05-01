package com.xiesx.fastboot.support.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.schlichtherle.license.AbstractKeyStoreParam;

/**
 * @title LicenseKeyStoreParam.java
 * @description 自定义KeyStoreParam，用于将公私钥存储文件存放到其他磁盘位置而不是项目中。现场使用的时候公钥大部分都不会放在项目中的
 * @author xiesx
 * @date 2020-7-21 22:34:17
 */
public class LicenseKeyStoreParam extends AbstractKeyStoreParam {

    /**
     * 公钥/私钥在磁盘上的存储路径
     */
    private String storePath;

    private String storePwd;

    private String alias;

    private String keyPwd;

    public LicenseKeyStoreParam(Class<?> clazz, String resource, String alias, String storePwd) {
        super(clazz, resource);
        this.storePath = resource;
        this.alias = alias;
        this.storePwd = storePwd;
    }

    public LicenseKeyStoreParam(Class<?> clazz, String resource, String alias, String storePwd, String keyPwd) {
        super(clazz, resource);
        this.storePath = resource;
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getKeyPwd() {
        return keyPwd;
    }

    @Override
    public String getStorePwd() {
        return storePwd;
    }

    /**
     * AbstractKeyStoreParam里面的getStream()方法默认文件是存储的项目中。 用于将公私钥存储文件存放到其他磁盘位置而不是项目中
     */
    @Override
    public InputStream getStream() throws IOException {
        return new FileInputStream(new File(storePath));
    }
}
