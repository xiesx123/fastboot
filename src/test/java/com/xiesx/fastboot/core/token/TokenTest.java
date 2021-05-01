package com.xiesx.fastboot.core.token;

import java.util.Map;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.base.config.Configed;
import com.xiesx.fastboot.core.token.cfg.TokenCfg;

import cn.hutool.core.date.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;


public class TokenTest {

    public static void main(String[] args) {
        //
        Map<String, Object> header = Maps.newConcurrentMap();
        header.put("user", "xxx");
        //
        Map<String, Object> claims = Maps.newConcurrentMap();
        claims.put(TokenCfg.UID, "123");
        //
        String token = "";
        // token = simple(Configed.FASTBOOT, "api");
        // token = simple(Configed.FASTBOOT, "api", JWT_EXPIRE_M_1);
        // token = simple(Configed.FASTBOOT, "api", claims, JWT_EXPIRE_M_1);
        token = JwtHelper.simple(Configed.FASTBOOT, "api", header, claims, JwtHelper.JWT_EXPIRE_D_1);
        System.out.println(token);
        //
        Jws<Claims> jws = JwtHelper.parser(token);
        //
        System.out.println("签名信息：" + jws.getSignature());

        JwsHeader<?> h = jws.getHeader();
        System.out.println("头部信息：" + h.getOrDefault("user", "-"));

        Claims c = jws.getBody();
        System.out.println("用户id：" + c.getId());
        System.out.println("主题：" + c.getSubject());
        System.out.println("签发者：" + c.getIssuer());
        System.out.println("接收者：" + c.getAudience());

        System.out.println("登录时间：" + DateUtil.formatDateTime(c.getIssuedAt()));
        System.out.println("过期时间：" + DateUtil.formatDateTime(c.getExpiration()));
        //
        System.out.println("是否过期时间：" + JwtHelper.isExpired(c.getExpiration()));
        //
        System.out.println("附加编号：" + c.getOrDefault(TokenCfg.UID, "-"));
    }
}
