package com.xiesx.fastboot;

import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;

/**
 * @title SpringHelper.java
 * @description Spring工具类，静态工具方法获取bean
 * @author xiesx
 * @date 2020-7-21 22:46:54
 */
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
    public static Object getBean(String name) {
        try {
            ApplicationContext applicationContext = SpringContext.getApplicationContext();
            if (applicationContext != null) {
                return applicationContext.getBean(name);
            }
        } catch (Exception e) {
            return null;
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
    public static <T> T getBean(Class<T> clazz) {
        try {
            ApplicationContext applicationContext = SpringContext.getApplicationContext();
            if (applicationContext != null) {
                return applicationContext.getBean(clazz);
            }
        } catch (Exception e) {
            return null;
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
    public static <T> T getBean(Class<T> clazz, String qualifier) {
        try {
            ApplicationContext applicationContext = SpringContext.getApplicationContext();
            if (applicationContext != null) {
                return BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(), clazz, qualifier);
            }
        } catch (Exception e) {
            return null;
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
    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            ApplicationContext applicationContext = SpringContext.getApplicationContext();
            if (applicationContext != null) {
                return applicationContext.getBean(name, clazz);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
