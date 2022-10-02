package com.xiesx.fastboot.core.event;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

/**
 * @title SimpleEventHandler.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:00
 */
@Log4j2
@Component
public class SimpleEventHandler extends EventAdapter<SimpleEvent> {

    @Override
    public boolean process(SimpleEvent e) throws InterruptedException {
        log.debug("==================== 收到{}事件 ===================", e.getName());
        if (e.isSleep()) {
            log.debug("==================== sleep ===================", e.getName());
            TimeUnit.SECONDS.sleep(2);
        }
        log.debug("==================== 结束{}事件 ===================", e.getName());
        return true;
    }
}
