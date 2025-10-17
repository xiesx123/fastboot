package com.xiesx.fastboot.core.event;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.ParameterizedType;

@Log4j2
public abstract class EventAdapter<T> {

    private static final String EVENT_METHOD = "process";

    public abstract boolean process(T t) throws Exception;

    @Subscribe
    @AllowConcurrentEvents
    public void onEvent(T event) {
        if (ObjectUtil.isNotNull(
                ReflectUtil.getMethod(this.getClass(), EVENT_METHOD, event.getClass()))) {
            try {
                ParameterizedType p = (ParameterizedType) this.getClass().getGenericSuperclass();
                Class<T> c = (Class<T>) p.getActualTypeArguments()[0];
                if (c.getName().equals(event.getClass().getName()) && !process(event)) {
                    log.warn("handle event {} fail", event.getClass());
                }
            } catch (Exception e) {
                log.error("handle event {} {} ", event.getClass(), e.getMessage());
            }
        }
    }
}
