package com.xiesx.fastboot;

import java.util.Optional;

import org.springframework.context.ApplicationContext;

import cn.hutool.extra.spring.SpringUtil;
import lombok.NonNull;

/**
 * @title SpringHelper.java
 * @description
 * @author xiesx
 * @date 2021-09-06 11:42:01
 */
public class SpringHelper {

    /**
     * 获取上下文
     *
     * @return
     */
    public static ApplicationContext getContext() {
        return SpringUtil.getApplicationContext();
    }

    /**
     * 通过class获取Bean
     *
     * @param clazz<T> Bean类
     * @return Bean对象
     */
    public static <T> T getBean(@NonNull Class<T> clazz) {
        return SpringUtil.getBean(clazz);
    }

    /**
     * 通过name获取Bean
     *
     * @param name
     * @return Bean对象
     */
    public static <T> T getBean(@NonNull String name) {
        return SpringUtil.getBean(name);
    }

    /**
     * 通过class、name获取Bean
     *
     * @param name
     * @param <T> Bean类型
     * @return Bean对象
     */
    public static <T> T getBean(@NonNull String name, @NonNull Class<T> clazz) {
        return SpringUtil.getBean(name, clazz);
    }

    /**
     * 是否存在
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> Optional<T> hasBean(@NonNull Class<T> clazz) {
        return Optional.ofNullable(getBean(clazz));
    }
}
