package com.xiesx.fastboot;

import java.util.Optional;

import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;

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
        return Optional.ofNullable(SpringContext.getApplicationContext()).orElse(null);
    }

    /**
     * 通过class获取Bean
     *
     * @param <T> Bean类型
     * @param clazz Bean类
     * @return Bean对象
     */
    public static <T> T getBean(@NonNull Class<T> clazz) {
        return Optional.ofNullable(getContext()).map(ac -> ac.getBean(clazz)).orElse(null);
    }

    /**
     * 通过class、name获取Bean
     *
     * @param <T> Bean类型
     * @param clazz Bean类
     * @return Bean对象
     */
    public static <T> T getBean(@NonNull Class<T> clazz, @NonNull String name) {
        return Optional.ofNullable(getContext()).map(ac -> ac.getBean(name, clazz)).orElse(null);
    }

    /**
     * 通过class获取Bean(Qulifier)
     *
     * @param clazz
     * @param qualifier
     * @return
     */
    public static <T> T getBean(@NonNull String qualifier, @NonNull Class<T> clazz) {
        return Optional.ofNullable(getContext()).map(ac -> BeanFactoryAnnotationUtils.qualifiedBeanOfType(ac.getAutowireCapableBeanFactory(), clazz, qualifier)).orElse(null);
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
