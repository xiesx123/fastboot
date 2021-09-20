package com.xiesx.fastboot;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
public class SpringStartup implements InitializingBean {

    public SpringStartup() {
        log.info("SpringStartup constructor");
    }

    @PostConstruct
    public void postConstruct() {
        log.info("SpringStartup postConstruct");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("SpringStartup afterPropertiesSet");
    }
}
