package com.xiesx.fastboot.core.eventbus.cfg;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.google.common.collect.Maps;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.eventbus.EventAdapter;
import com.xiesx.fastboot.core.eventbus.EventBusHelper;
import com.xiesx.fastboot.core.eventbus.base.Event;

import cn.hutool.core.lang.Singleton;
import lombok.extern.log4j.Log4j2;

/**
 * @title EventBusConfiguration.java
 * @description
 * @author xiesx
 * @date 2021-04-24 01:34:57
 */
@Log4j2
@SuppressWarnings({"all", "unchecked"})
public class EventBusCfg {

    private Map<String, EventAdapter> beans = Maps.newConcurrentMap();

    public EventBusCfg() {
        EventBusHelper.getEventBus().register(new Object() {

            @Subscribe
            public void lister(DeadEvent event) {
                log.warn("from {} Dead Event : {}", event.getSource().getClass().getSimpleName(), event.getEvent());
            }
        });
    }

    @PostConstruct
    public void construct() throws Exception {
        beans.putAll(SpringHelper.getContext().getBeansOfType(EventAdapter.class));
        if (!beans.isEmpty()) {
            for (EventAdapter<? extends Event> eventAbstract : beans.values()) {
                EventBusHelper.register(eventAbstract);
            }
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        if (!beans.isEmpty()) {
            for (EventAdapter<? extends Event> eventAbstract : beans.values()) {
                EventBusHelper.unRegister(eventAbstract);
            }
        }
        Singleton.remove(EventBusHelper.class.getName());
    }
}
