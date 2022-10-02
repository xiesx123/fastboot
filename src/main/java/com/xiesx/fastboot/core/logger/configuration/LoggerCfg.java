package com.xiesx.fastboot.core.logger.configuration;

import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xiesx.fastboot.core.logger.LoggerAspect;
import com.xiesx.fastboot.core.logger.interceptor.LogInterceptor;

/**
 * @title LoggerCfg.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:02:56
 */
@Import({LoggerAspect.class})
public class LoggerCfg implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加处理器
        registry.addInterceptor(new LogInterceptor());
    }
}
