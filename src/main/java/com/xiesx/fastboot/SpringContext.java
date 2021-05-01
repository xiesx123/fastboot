package com.xiesx.fastboot;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
        if (ObjectUtil.isNotNull(applicationContext)) {
            SpringStartup.init();
            SpringStartup.scheduler();
            log.info("Spring ApplicationContext completed.");
        }
    }
}
