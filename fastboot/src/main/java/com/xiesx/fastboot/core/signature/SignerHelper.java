package com.xiesx.fastboot.core.signature;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import java.util.Map;

public class SignerHelper {

  public static String getSignature(Map<String, ?> param, String secret) {
    secret = Opt.ofBlankAble(secret).orElse(StrUtil.EMPTY);
    return SecureUtil.signParams(DigestAlgorithm.MD5, param, secret);
  }
}
