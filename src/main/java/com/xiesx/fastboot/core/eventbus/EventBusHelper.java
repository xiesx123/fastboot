package com.xiesx.fastboot.core.eventbus;

import java.util.concurrent.Executors;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.xiesx.fastboot.base.config.Configed;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.func.Func0;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * @title EventBusHelper.java
 * @description
 * @author xiesx
 * @date 2021-04-24 01:35:06
 */
@Log4j2
public class EventBusHelper {

    private final static EventBus eventbus = Singleton.get(EventBusHelper.class.getName(), new Func0<EventBus>() {

        private static final long serialVersionUID = 1L;

        @Override
        public EventBus call() throws Exception {
            return new AsyncEventBus(Configed.FASTBOOT, Executors.newCachedThreadPool());
        }
    });

    /**
     * 获取事件总线
     *
     * @return
     */
    public static EventBus getEventBus() {
        return eventbus;
    }

    /**
     * 注册
     *
     * @param handler
     */
    public static void register(@NonNull EventAdapter<? extends AbstractEvent> handler) {
        eventbus.register(handler);
        log.info("Registered event : {}", handler.getClass());
    }

    /**
     * 发布消息
     *
     * @param event
     */
    public static void post(@NonNull Object object) {
        eventbus.post(object);
    }

    /**
     * 发布消息
     *
     * @param event
     */
    public static void post(@NonNull AbstractEvent event) {
        eventbus.post(event);
    }

    /**
     * 注销
     *
     * @param handler
     */
    public static void unregister(@NonNull EventAdapter<? extends AbstractEvent> handler) {
        eventbus.unregister(handler);
        log.info("Unregisted event : {}", handler.getClass());
    }
}
