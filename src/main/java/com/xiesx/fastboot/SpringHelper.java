package com.xiesx.fastboot;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;

import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class SpringHelper {

    /** 获取项目地址 */
    public static String getUrl() {
        String host = NetUtil.getLocalhost().getHostAddress();
        String port = SpringUtil.getProperty("server.port");
        String path = SpringUtil.getProperty("server.servlet.context-path");
        return StrUtil.format("http://{}:{}{}", host, port, path);
    }

    /** 获取上下文 */
    public static ApplicationContext getContext() {
        return SpringUtil.getApplicationContext();
    }

    /** 通过class获取Bean */
    public static <T> T getBean(Class<T> clazz) {
        return SpringUtil.getBean(clazz);
    }

    /** 通过name获取Bean */
    public static <T> T getBean(String name) {
        return SpringUtil.getBean(name);
    }

    /** 通过class、name获取Bean */
    public static <T> T getBean(String name, Class<T> clazz) {
        return SpringUtil.getBean(name, clazz);
    }

    /** 检查是否存在Bean */
    public static <T> Optional<T> hasBean(Class<T> clazz) {
        return Optional.ofNullable(getBean(clazz));
    }
}
