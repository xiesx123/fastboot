package com.xiesx.fastboot.core.event;

import cn.hutool.core.lang.Singleton;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.xiesx.fastboot.base.config.Configed;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.Executors;

@Log4j2
public class EventBusHelper {

    private static final EventBus eventbus =
            Singleton.get(
                    EventBusHelper.class.getName(),
                    () -> new AsyncEventBus(Configed.FASTBOOT, Executors.newCachedThreadPool()));

    /** 获取事件总线 */
    public static EventBus getEventBus() {
        return eventbus;
    }

    /** 注册 */
    public static void register(@NonNull EventAdapter<?> handler) {
        eventbus.register(handler);
        log.debug("registered event : {}", handler.getClass());
    }

    /** 发布 */
    public static void post(@NonNull Object object) {
        eventbus.post(object);
    }

    /** 注销 */
    public static void unregister(@NonNull EventAdapter<?> handler) {
        eventbus.unregister(handler);
        log.debug("unregisted event : {}", handler.getClass());
    }
}
