package com.xiesx.fastboot.core.token;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.xiesx.fastboot.base.config.Configed;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.*;

/**
 * @title JwtHelper.java
 * @description
 * @author xiesx
 * @date 2020-7-21 22:37:47
 */
public class JwtHelper {

    /**
     * jwt密钥
     */
    public static final String JWT_SECRET = "fastboot-jwt";

    /**
     * jwt生成方
     */
    private final static String JWT_ISSUER = Configed.FASTBOOT;

    /**
     * jwt有效时间
     */
    public static final int JWT_EXPIRE_M_1 = 1 * 60 * 1000; // 1分钟

    public static final int JWT_EXPIRE_M_5 = 5 * JWT_EXPIRE_M_1; // 5分钟

    public static final int JWT_EXPIRE_H_1 = 1 * 60 * 60 * 1000; // 1小时

    public static final int JWT_EXPIRE_H_12 = 12 * JWT_EXPIRE_H_1; // 12小时

    public static final int JWT_EXPIRE_D_1 = 24 * 60 * 60 * 1000; // 1天

    public static final int JWT_EXPIRE_D_7 = 7 * JWT_EXPIRE_D_1; // 7天

    public static final int JWT_EXPIRE_D_30 = 30 * JWT_EXPIRE_D_1; // 30天

    /**
     * 生成jwt
     *
     * @return
     */
    public static String simple(String subject, String audience) {
        return create(IdUtil.fastSimpleUUID(), subject, JWT_ISSUER, audience, null, null, JWT_EXPIRE_D_1);
    }

    public static String simple(String subject, String audience, int timeout) {
        return create(IdUtil.fastSimpleUUID(), subject, JWT_ISSUER, audience, null, null, timeout);
    }

    public static String simple(String subject, String audience, Map<String, Object> claim, int timeout) {
        return create(IdUtil.fastSimpleUUID(), subject, JWT_ISSUER, audience, null, claim, timeout);
    }

    public static String simple(String subject, String audience, Map<String, Object> header, Map<String, Object> claim, int timeout) {
        return create(IdUtil.fastSimpleUUID(), subject, JWT_ISSUER, audience, header, claim, timeout);
    }

    public static String simple(String subject, String issuer, String audience, Map<String, Object> claim, int timeout) {
        return create(IdUtil.fastSimpleUUID(), subject, issuer, audience, null, claim, timeout);
    }

    public static String simple(String subject, String issuer, String audience, Map<String, Object> header, Map<String, Object> claim, int timeout) {
        return create(IdUtil.fastSimpleUUID(), subject, issuer, audience, header, claim, timeout);
    }

    /**
     * @param jwtid 唯一身份标识
     * @param subject 主题
     * @param issuer 签发者
     * @param audience 接收者
     * @param header 头部信息
     * @param claim 私有属性
     * @param timeout 头部信息
     * @return
     */
    public static String create(String jwtid, String subject, String issuer, String audience, Map<String, Object> header, Map<String, Object> claim, int timeout) {
        // 开始时间
        Date staDate = DateUtil.date();
        // 结束时间
        Date endDate = DateUtil.offsetMillisecond(staDate, timeout);
        // 头部信息
        Map<String, Object> headers = MapUtil.newConcurrentHashMap(header);
        // 附加信息
        Map<String, Object> claims = MapUtil.newConcurrentHashMap(claim);
        // 构造
        JwtBuilder builder = Jwts.builder().setId(jwtid) // 唯一身份标识，根据业务需要，可以设置为一个不重复的值，主要用来作为一次性token，从而回避重放攻击
                .setSubject(subject) // 主题
                .setIssuer(issuer) // 签发者
                .setAudience(audience) // 接收者
                .setIssuedAt(staDate) // 签发时间
                .setExpiration(endDate) // 过期时间
                .setHeader(headers)// 头部信息
                .addClaims(claims)// 私有属性
                .signWith(SignatureAlgorithm.HS256, key());// 签名算法以及密匙
        return builder.compact();
    }

    /**
     * 解析
     *
     * @param token
     * @return
     */
    public static Jws<Claims> parser(String token) {
        return Jwts.parser().setSigningKey(key()).parseClaimsJws(token);
    }

    /**
     * 过期
     *
     * @param token
     * @return
     */
    public static boolean isExpired(Date expiration) {
        return expiration.before(new Date());
    }

    /**
     * 生成加密key
     *
     * @return
     */
    public static SecretKey key() {
        byte[] encodedKey = Base64.decodeBase64(JWT_SECRET);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }
}
