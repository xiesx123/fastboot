package com.xiesx.fastboot.core.event;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class SimpleEventHandler extends EventAdapter<SimpleEvent> {

    @Override
    public boolean process(SimpleEvent e) throws InterruptedException {
        log.info("==================== 收到【{}】事件 ===================", e.getName());
        if (e.isSleep()) {
            log.info("sleep", e.getName());
            TimeUnit.SECONDS.sleep(2);
        }
        log.info("==================== 结束【{}】事件 ===================", e.getName());
        return true;
    }
}
