package com.xiesx.fastboot.test.event;

import cn.hutool.core.thread.ThreadUtil;

import com.xiesx.fastboot.core.event.EventAdapter;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SimpleEventHandler extends EventAdapter<SimpleEvent> {

    @Override
    public boolean process(SimpleEvent e) {
        log.info("==================== 收到【{}】事件 ===================", e.getName());
        if (e.isSleep()) {
            log.info("sleep", e.getName());
            ThreadUtil.safeSleep(2000);
        }
        log.info("==================== 结束【{}】事件 ===================", e.getName());
        return true;
    }
}
