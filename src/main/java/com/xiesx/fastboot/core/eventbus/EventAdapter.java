package com.xiesx.fastboot.core.eventbus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.log4j.Log4j2;

/**
 * @title EventAdapter.java
 * @description
 * @author xiesx
 * @date 2021-04-24 01:34:31
 */
@Log4j2
@SuppressWarnings({"unchecked", "all"})
public abstract class EventAdapter<E extends AbstractEvent> {

    private static final String EVENT_METHOD = "process";

    public abstract boolean process(E e) throws Exception;

    @Subscribe
    @AllowConcurrentEvents
    public void onEvent(AbstractEvent event) {
        if (ReflectUtil.getMethod(this.getClass(), EVENT_METHOD, event.getClass()) != null) {
            try {
                if (!process((E) event)) {
                    log.warn("handle event {} fail", event.getClass());
                }
            } catch (Exception e) {
                log.error(String.format("handle event %s exception", event.getClass()));
            }
        }
    }
}
