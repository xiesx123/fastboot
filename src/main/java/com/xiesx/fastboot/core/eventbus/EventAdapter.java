package com.xiesx.fastboot.core.eventbus;

import org.springframework.util.ReflectionUtils;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.xiesx.fastboot.core.eventbus.base.BaseEvent;

import lombok.extern.log4j.Log4j2;

/**
 * @title EventAdapter.java
 * @description
 * @author xiesx
 * @date 2021-04-24 01:34:31
 */
@Log4j2
public abstract class EventAdapter<E extends BaseEvent> {

    private static final String METHOD_NAME = "process";

    @SuppressWarnings("unchecked")
    @Subscribe
    @AllowConcurrentEvents
    public void onEvent(BaseEvent event) {
        if (ReflectionUtils.findMethod(this.getClass(), METHOD_NAME, event.getClass()) != null) {
            try {
                if (!process((E) event)) {
                    log.warn("handle event {} fail", event.getClass());
                }
            } catch (Exception e) {
                log.error(String.format("handle event %s exception", event.getClass()), e);
            }
        }
    }

    public abstract boolean process(E e) throws Exception;
}
