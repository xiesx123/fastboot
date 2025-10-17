package com.xiesx.fastboot.core.logger.configuration;

import com.xiesx.fastboot.core.logger.LoggerAspect;
import com.xiesx.fastboot.core.logger.interceptor.LogInterceptor;

import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Import({LoggerAspect.class})
public class LoggerCfg implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加处理器
        registry.addInterceptor(new LogInterceptor());
    }
}
