package com.xiesx.fastboot.core.token;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.jwt.JWT;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.base.config.Configed;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JwtHelper {

  /** 有效时间 */
  public static final long JWT_EXPIRE_M_1 = TimeUnit.MINUTES.toSeconds(1); // 1分钟

  public static final long JWT_EXPIRE_M_5 = TimeUnit.MINUTES.toSeconds(5); // 5分钟

  public static final long JWT_EXPIRE_H_1 = TimeUnit.HOURS.toSeconds(1); // 1小时

  public static final long JWT_EXPIRE_H_12 = TimeUnit.HOURS.toSeconds(12); // 12小时

  public static final long JWT_EXPIRE_D_1 = TimeUnit.DAYS.toSeconds(1); // 1天

  public static final long JWT_EXPIRE_D_7 = TimeUnit.DAYS.toSeconds(7); // 7天

  public static final long JWT_EXPIRE_D_30 = TimeUnit.DAYS.toSeconds(30); // 30天

  /** 生成 */
  public static String build(long timeout, String audience, String secret) {
    return build(
        IdUtil.fastSimpleUUID(),
        Configed.FASTBOOT,
        Configed.FASTBOOT,
        timeout,
        Maps.newHashMap(),
        Maps.newHashMap(),
        audience,
        secret);
  }

  public static String build(
      long timeout,
      Map<String, Object> header,
      Map<String, Object> claim,
      String audience,
      String secret) {
    return build(
        IdUtil.fastSimpleUUID(),
        Configed.FASTBOOT,
        Configed.FASTBOOT,
        timeout,
        header,
        claim,
        audience,
        secret);
  }

  public static String build(
      String id,
      String subject,
      String issuer,
      long timeout,
      Map<String, ?> header,
      Map<String, ?> claim,
      String audience,
      String secret) {
    // 开始时间
    Date staDate = DateUtil.date();
    // 结束时间
    Date endDate = DateUtil.offsetSecond(staDate, (int) timeout);
    return create(id, subject, issuer, staDate, endDate, header, claim, audience, secret);
  }

  /** 创建令牌 */
  public static String create(
      String id,
      String subject,
      String issuer,
      Date staDate,
      Date endDate,
      Map<String, ?> header,
      Map<String, ?> claim,
      String audience,
      String secret) {
    return JWT.create() //
        .setJWTId(id) // 唯一身份标识，根据业务需要，可以设置为一个不重复的值，主要用来作为一次性token，从而回避重放攻击
        .setSubject(subject) // 主题
        .setIssuer(issuer) // 签发者
        .setIssuedAt(staDate) // 签发时间
        .setExpiresAt(endDate) // 过期时间
        .setNotBefore(DateUtil.date()) // 生效时间
        .addHeaders(MapUtil.newConcurrentHashMap(header)) // 头部信息
        .addPayloads(MapUtil.newConcurrentHashMap(claim)) // 私有属性
        .setAudience(audience) // 接收者
        .setKey(secret.getBytes()) // 密匙
        .sign();
  }

  /** 解析 */
  public static JWT parser(String secret, String token) {
    return JWT.of(token).setKey(secret.getBytes());
  }

  /** 验证 */
  public static boolean validate(String secret, String token) {
    try {
      return JWT.of(token).setKey(secret.getBytes()).verify();
    } catch (Exception e) {
      return false;
    }
  }
}
