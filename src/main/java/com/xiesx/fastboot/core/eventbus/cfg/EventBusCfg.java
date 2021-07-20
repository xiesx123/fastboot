package com.xiesx.fastboot.core.eventbus.cfg;

import java.util.Map;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import com.google.common.collect.Maps;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.eventbus.AbstractEvent;
import com.xiesx.fastboot.core.eventbus.EventAdapter;
import com.xiesx.fastboot.core.eventbus.EventBusHelper;

import cn.hutool.core.lang.Singleton;
import lombok.extern.log4j.Log4j2;

/**
 * @title EventBusCfg.java
 * @description
 * @author xiesx
 * @date 2021-04-24 01:34:57
 */
@Log4j2
@Configuration
@SuppressWarnings({"all", "unchecked"})
public class EventBusCfg implements DisposableBean, ApplicationListener<ContextRefreshedEvent> {

    private Map<String, EventAdapter> beans = Maps.newConcurrentMap();

    public EventBusCfg() {
        EventBusHelper.getEventBus().register(new Object() {

            @Subscribe
            public void lister(DeadEvent event) {
                log.warn("from {} Dead Event : {}", event.getSource().getClass().getSimpleName(), event.getEvent());
            }
        });
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            beans.putAll(SpringHelper.getContext().getBeansOfType(EventAdapter.class));
            if (!beans.isEmpty()) {
                for (EventAdapter<? extends AbstractEvent> eventAbstract : beans.values()) {
                    EventBusHelper.register(eventAbstract);
                }
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        if (!beans.isEmpty()) {
            for (EventAdapter<? extends AbstractEvent> eventAbstract : beans.values()) {
                EventBusHelper.unregister(eventAbstract);
            }
        }
        Singleton.remove(EventBusHelper.class.getName());
    }
}
