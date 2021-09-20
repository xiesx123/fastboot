package com.xiesx.fastboot.core.token;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.base.config.Configed;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import io.jsonwebtoken.*;

/**
 * @title JwtHelper.java
 * @description
 * @author xiesx
 * @date 2020-7-21 22:37:47
 */
public class JwtHelper {

    /**
     * 密钥
     */
    public static final String JWT_SECRET = "fastboot-jwt";

    /**
     * 生成方
     */
    private final static String JWT_ISSUER = Configed.FASTBOOT;

    /**
     * 有效时间
     */
    public static final long JWT_EXPIRE_M_1 = TimeUnit.MINUTES.toSeconds(1); // 1分钟

    public static final long JWT_EXPIRE_M_5 = TimeUnit.MINUTES.toSeconds(5); // 5分钟

    public static final long JWT_EXPIRE_H_1 = TimeUnit.HOURS.toSeconds(1); // 1小时

    public static final long JWT_EXPIRE_H_12 = TimeUnit.HOURS.toSeconds(12); // 12小时

    public static final long JWT_EXPIRE_D_1 = TimeUnit.DAYS.toSeconds(1); // 1天

    public static final long JWT_EXPIRE_D_7 = TimeUnit.DAYS.toSeconds(7); // 7天

    public static final long JWT_EXPIRE_D_30 = TimeUnit.DAYS.toSeconds(30); // 30天

    /**
     * 生成
     *
     * @return
     */
    public static String simple(String subject, String audience) {
        return create(IdUtil.fastSimpleUUID(), subject, JWT_ISSUER, audience, null, null, JWT_EXPIRE_D_1, JWT_SECRET);
    }

    public static String simple(String subject, String audience, long timeout) {
        return create(IdUtil.fastSimpleUUID(), subject, JWT_ISSUER, audience, Maps.newHashMap(), Maps.newHashMap(), timeout, JWT_SECRET);
    }

    public static String simple(String subject, String audience, Map<String, Object> claim, long timeout) {
        return create(IdUtil.fastSimpleUUID(), subject, JWT_ISSUER, audience, Maps.newHashMap(), claim, timeout, JWT_SECRET);
    }

    public static String simple(String subject, String audience, Map<String, Object> header, Map<String, Object> claim, long timeout) {
        return create(IdUtil.fastSimpleUUID(), subject, JWT_ISSUER, audience, header, claim, timeout, JWT_SECRET);
    }

    public static String simple(String subject, String issuer, String audience, Map<String, Object> claim, long timeout) {
        return create(IdUtil.fastSimpleUUID(), subject, issuer, audience, Maps.newHashMap(), claim, timeout, JWT_SECRET);
    }

    public static String simple(String subject, String issuer, String audience, Map<String, Object> header, Map<String, Object> claim, long timeout) {
        return create(IdUtil.fastSimpleUUID(), subject, issuer, audience, header, claim, timeout, JWT_SECRET);
    }

    /**
     * 创建令牌
     *
     * @param id 唯一身份标识
     * @param subject 主题
     * @param issuer 签发者
     * @param audience 接收者
     * @param header 头部信息
     * @param claim 私有属性
     * @param timeout 头部信息
     * @param secret 密匙
     * @return
     */
    public static String create(String id, String subject, String issuer, String audience, Map<String, Object> header, Map<String, Object> claim, long timeout, String secret) {
        // 开始时间
        Date staDate = DateUtil.date();
        // 结束时间
        Date endDate = DateUtil.offsetSecond(staDate, (int) timeout);
        // 头部信息
        Map<String, Object> headers = MapUtil.newConcurrentHashMap(header);
        // 附加信息
        Map<String, Object> claims = MapUtil.newConcurrentHashMap(claim);
        // 构造
        JwtBuilder builder = Jwts.builder()//
                .setId(id) // 唯一身份标识，根据业务需要，可以设置为一个不重复的值，主要用来作为一次性token，从而回避重放攻击
                .setSubject(subject) // 主题
                .setIssuer(issuer) // 签发者
                .setAudience(audience) // 接收者
                .setIssuedAt(staDate) // 签发时间
                .setExpiration(endDate) // 过期时间
                .setHeader(headers)// 头部信息
                .addClaims(claims)// 私有属性
                .signWith(SignatureAlgorithm.HS256, key(secret));// 签名算法以及密匙
        return builder.compact();
    }

    /**
     * 解析令牌
     *
     * @param secret
     * @param token
     * @return
     */
    public static Jws<Claims> parser(String secret, String token) {
        return Jwts.parser().setSigningKey(key(secret)).parseClaimsJws(token);
    }

    public static Jws<Claims> parser(String token) {
        return Jwts.parser().setSigningKey(key(JWT_SECRET)).parseClaimsJws(token);
    }

    /**
     * 生成密钥
     *
     * @return
     */
    public static SecretKey key(String secret) {
        return SecureUtil.generateKey("AES", Base64.encode(secret).getBytes());
    }

    /**
     * 是否过期
     *
     * @param token
     * @return
     */
    public static boolean isExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
