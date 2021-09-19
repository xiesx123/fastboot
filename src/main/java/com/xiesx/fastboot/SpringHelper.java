package com.xiesx.fastboot;

import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * @title SpringHelper.java
 * @description
 * @author xiesx
 * @date 2021-09-06 11:42:01
 */
@Log4j2
public class SpringHelper {

    /**
     * applicationContext
     *
     * @return
     */
    public static ApplicationContext getContext() {
        ApplicationContext applicationContext = SpringContext.getApplicationContext();
        if (applicationContext != null) {
            return applicationContext;
        }
        return null;
    }

    /**
     * 通过name获取 Bean
     *
     * @param <T> Bean类型
     * @param name Bean名称
     * @return Bean
     */
    public static Object getBean(@NonNull String name) {
        try {
            ApplicationContext applicationContext = SpringContext.getApplicationContext();
            if (applicationContext != null) {
                return applicationContext.getBean(name);
            }
        } catch (Exception e) {
            log.error("spring get bean error", e);
        }
        return null;
    }

    /**
     * 通过class获取Bean
     *
     * @param <T> Bean类型
     * @param clazz Bean类
     * @return Bean对象
     */
    public static <T> T getBean(@NonNull Class<T> clazz) {
        try {
            ApplicationContext applicationContext = SpringContext.getApplicationContext();
            if (applicationContext != null) {
                return applicationContext.getBean(clazz);
            }
        } catch (Exception e) {
            log.error("spring get bean error", e);
        }
        return null;
    }

    /**
     * 通过class，Qulifier值取同类型的某个bean
     *
     * @param clazz
     * @param qualifier
     * @return
     */
    public static <T> T getBean(@NonNull Class<T> clazz, @NonNull String qualifier) {
        try {
            ApplicationContext applicationContext = SpringContext.getApplicationContext();
            if (applicationContext != null) {
                return BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(), clazz, qualifier);
            }
        } catch (Exception e) {
            log.error("spring get bean error", e);
        }
        return null;
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param <T> bean类型
     * @param name Bean名称
     * @param clazz bean类型
     * @return Bean对象
     */
    public static <T> T getBean(@NonNull String name, @NonNull Class<T> clazz) {
        try {
            ApplicationContext applicationContext = SpringContext.getApplicationContext();
            if (applicationContext != null) {
                return applicationContext.getBean(name, clazz);
            }
        } catch (Exception e) {
            log.error("spring get bean error", e);
        }
        return null;
    }
}
