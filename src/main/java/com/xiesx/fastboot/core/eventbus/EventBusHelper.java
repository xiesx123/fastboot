package com.xiesx.fastboot.core.eventbus;

import java.util.concurrent.Executors;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.xiesx.fastboot.base.config.Configed;
import com.xiesx.fastboot.core.eventbus.base.Event;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.func.Func0;
import lombok.extern.log4j.Log4j2;

/**
 * @title EventBusFacade.java
 * @description
 * @author xiesx
 * @date 2021-04-24 01:35:06
 */
@Log4j2
public class EventBusHelper {

    private final static EventBus bus = Singleton.get(EventBusHelper.class.getName(), new Func0<EventBus>() {

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
        return bus;
    }

    /**
     * 发布消息
     * 
     * @param event
     */
    public static void post(Object object) {
        if (object == null) {
            return;
        }
        bus.post(object);
    }

    /**
     * 发布消息
     * 
     * @param event
     */
    public static void submit(Event event) {
        if (event == null) {
            return;
        }
        bus.post(event);
    }

    /**
     * 注册
     * 
     * @param handler
     */
    public static void register(EventAdapter<? extends Event> handler) {
        if (handler == null) {
            return;
        }
        bus.register(handler);
        log.info("Registered event : {}", handler.getClass());
    }

    /**
     * 注销
     * 
     * @param handler
     */
    public static void unRegister(EventAdapter<? extends Event> handler) {
        if (handler == null) {
            return;
        }
        bus.unregister(handler);
        log.info("Unregisted event : {}", handler.getClass());
    }
}
