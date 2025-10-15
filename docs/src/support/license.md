该功能由[`TrueLicense`](https://truelicense.namespace.global/)封装而来

> License，即版权许可证，一般用于收费软件给付费用户提供的访问许可证明。根据应用部署位置的不同，一般可以分为以下两种情况讨论：
>
> - 1、应用部署在开发者自己的云服务器上。这种情况下用户通过账号登录的形式远程访问，因此只需要在账号登录的时候校验目标账号的有效期、访问权限等信息即可。
> - 2、应用部署在客户的内网环境。因为这种情况开发者无法控制客户的网络环境，也不能保证应用所在服务器可以访问外网，因此通常的做法是使用服务器许可文件，在应用启动的时候加载证书，然后在登录或者其他关键操作的地方校验证书的有效性。

# 生成密钥

```
@E:

@mkdir  key

@cd key

# 1、KeyTool工具来生成密钥对
# validity：私钥的有效期多少天
# alias：私钥别称A
# keystore: 私钥文件名B
# storepass：私钥库密码C
# keypass：私钥密码D
@keytool -genkeypair -keysize 1024 -validity 3650 -alias "privateKey" -keystore "privateKeys.store" -storepass "FastBoot" -keypass "gotv" -dname "CN=GOTV, OU=GOTV, O=GOTV, L=WUHAN, ST=HUBEI, C=CN"

# 2、把私匙库内的公匙导出到一个文件当中
# alias：私钥别称A
# keystore：私钥文件名B
# storepass: 私钥库密码C
# file：证书名称
@keytool -exportcert -alias "privateKey" -keystore "privateKeys.store" -storepass "FastBoot" -file "certfile.cer"

# 3、再把这个证书文件导入到公匙库
# alias：公钥别称E
# file：证书名称
# keystore：公钥文件名F
# storepass：私钥库密码C
echo y | keytool -import -alias "publicCert" -file "certfile.cer" -keystore "publicCerts.store" -storepass "FastBoot"

@pause
```

注意：上述内容复制后新建后缀名 bat 文件，点击运行后会在当前目录下面得到三个文件

- privateKeys.keystore：私钥，这个甲方备份，续期时使用，不能泄露给别人。
- publicCerts.keystore： 公钥，这个乙方使用，验证 license 文件里面的信息的。
- certfile.cer：证书文件，可以删掉。

# 测试示例

```
@Test
public void creator() {
    // 生效时间
    Date issusedDate = new Date();
    log.info("license generate issusedDate {}", format.format(issusedDate));
    // 过期时间
    Date expiryDate = DateUtils.addDays(issusedDate, 30);
    log.info("license generate expiryDate {}", format.format(expiryDate));
    // 额外信息
    LicenseParamsExtra licenseExtraModel = new LicenseParamsExtra();
    // 构造参数
    LicenseParams params = new LicenseParams()
            // 证书主题
            .setSubject(subject)
            // 密钥库密码
            .setStorePass(storepass)
            // 私钥路径
            .setPrivateKeysStorePath(LicenseHelperTest.class.getResource("privateKeys.store").getPath())
            // 私钥别称
            .setPrivateAlias("privateKey")
            // 私钥密码
            .setPrivatePass(privatepass)
            // 证书生成路径
            .setLicensePath(LicenseHelperTest.class.getResource("license.lic").getPath())
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
public void verify() throws Exception {
    // 证书信息
    LicenseVerify param = new LicenseVerify()
            // 证书主题
            .setSubject(subject)
            // 公钥路径
            .setPublicKeysStorePath("E:/license/publicCerts.store")
            // 公钥别称
            .setPublicAlias("publicCert")
            // 密钥库密码
            .setStorePass(storepass)
            // 证书路径
            .setLicensePath("E:/license/license.lic");
    // 安装证书
    param.install();
    // 校验证书
    param.verify();
}
```

# 验证结果

```
[FastBoot][ INFO][08-22 16:42:04]-->[main: 1436][creator(LicenseHelperTest.java:33)] | - license generate issusedDate 2020-08-22 16:42:04
[FastBoot][ INFO][08-22 16:42:04]-->[main: 1440][creator(LicenseHelperTest.java:36)] | - license generate expiryDate 2020-09-21 16:42:04
[FastBoot][ INFO][08-22 16:42:04]-->[main: 1603][creator(LicenseHelperTest.java:61)] | - license generate success
[FastBoot][ INFO][08-22 16:42:04]-->[main: 1401][install(LicenseVerify.java:92)] | - license install success
[FastBoot][ INFO][08-22 16:42:04]-->[main: 1424][verify(LicenseVerify.java:115)] | - license verify success 2020-08-22 16:42:04
 - 2020-09-21 16:42:04
```

# 注意事项

```
[FastBoot][ INFO][08-22 19:01:13]-->[main: 3158][install(LicenseVerify.java:92)] | - license install success
[FastBoot][ INFO][08-22 19:01:13]-->[main: 3171][verify(LicenseVerify.java:115)] | - license verify success 2020-08-22 16:42:04
 - 2020-09-21 16:42:04
```

后续会同步更新授权版本，与开源版如下不同

- 验证证书，授权失败终止进程
- 代码混淆，混淆所有相关代码
