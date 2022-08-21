package com.xiesx.fastboot.core.signature;

import java.util.Map;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import lombok.NonNull;

/**
 * @title SignerHelper.java
 * @description 数据签名帮助类
 * @author xiesx
 * @date 2020-7-21 22:35:39
 */
public class SignerHelper {

    /**
     * 获取签名
     *
     * @param param
     * @param key
     * @return
     */
    public static String getSignature(@NonNull Map<String, String> param, @NonNull String key) {
        return SecureUtil.signParams(DigestAlgorithm.MD5, MapUtil.sort(param), key);
    }
}
