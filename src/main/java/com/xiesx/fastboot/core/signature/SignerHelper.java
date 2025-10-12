package com.xiesx.fastboot.core.signature;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;

import lombok.NonNull;

import java.util.Map;

public class SignerHelper {

    public static String getSignature(@NonNull Map<String, ?> param, @NonNull String key) {
        return SecureUtil.signParams(DigestAlgorithm.MD5, param, key);
    }
}
