package com.xiesx.fastboot.core.signer;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;

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
    public static String getSignature(Map<String, Object> param, String key) {
        return SecureUtil.md5(getSortParams(param) + "&key=" + key);
    }

    /**
     * 按key进行正序排列，之间以&相连
     *
     * @param params
     * @return
     */
    public static String getSortParams(Map<String, Object> params) {
        TreeMap<String, Object> map = MapUtil.sort(params);
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        String str = "";
        while (iter.hasNext()) {
            String key = iter.next();
            Object value = map.get(key);
            str += key + "=" + value + "&";
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
