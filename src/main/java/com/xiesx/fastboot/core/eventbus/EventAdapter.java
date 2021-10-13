package com.xiesx.fastboot.core.eventbus;

import java.lang.reflect.ParameterizedType;

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
public abstract class EventAdapter<T> {

    private static final String EVENT_METHOD = "process";

    public abstract boolean process(T t) throws Exception;

    @Subscribe
    @AllowConcurrentEvents
    public void onEvent(T event) {
        if (ReflectUtil.getMethod(this.getClass(), EVENT_METHOD, event.getClass()) != null) {
            try {
                ParameterizedType p = (ParameterizedType) this.getClass().getGenericSuperclass();
                Class<T> c = (Class<T>) p.getActualTypeArguments()[0];
                if (c.getName().equals(event.getClass().getName())) {
                    if (!process((T) event)) {
                        log.warn("handle event {} fail", event.getClass());
                    }
                }
            } catch (Exception e) {
                log.error("handle event {} {} ", event.getClass(), e.getMessage());
            }
        }
    }
}
