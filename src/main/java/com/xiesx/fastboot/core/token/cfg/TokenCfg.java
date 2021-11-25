package com.xiesx.fastboot.core.token.cfg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xiesx.fastboot.core.token.interceptor.TokenInterceptor;
import com.xiesx.fastboot.core.token.processor.RequestHeaderMethodProcessor;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.jwt.JWT;

/**
 * @title TokenCfg.java
 * @description 令牌认证
 * @author xiesx
 * @date 2020-7-21 22:36:02
 */
@EnableConfigurationProperties(TokenProperties.class)
// @ConditionalOnProperty(prefix = TokenProperties.PREFIX, name = "enabled", havingValue = "true",
// matchIfMissing = true)
@ConditionalOnClass({JWT.class})
public class TokenCfg implements WebMvcConfigurer {

    public static final String UID = "uid";

    @Autowired
    TokenProperties mTokenProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加处理器
        InterceptorRegistration in = registry.addInterceptor(new TokenInterceptor());
        // 处理url
        ListUtil.toList(mTokenProperties.getIncludePaths()).forEach(path -> {
            in.addPathPatterns(path);
        });
        // 排除处理url
        ListUtil.toList(mTokenProperties.getExcludePaths()).forEach(path -> {
            in.excludePathPatterns(path);
        });
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestHeaderMethodProcessor());
    }
}
