package com.xiesx.fastboot.core.event.configuration;

import cn.hutool.core.lang.Singleton;

import com.google.common.collect.Maps;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.event.EventAdapter;
import com.xiesx.fastboot.core.event.EventBusHelper;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;

@Log4j2
@Configuration
public class EventBusCfg implements DisposableBean, ApplicationListener<ContextRefreshedEvent> {

    private Map<String, EventAdapter> beans = Maps.newConcurrentMap();

    public EventBusCfg() {
        EventBusHelper.getEventBus()
                .register(
                        new Object() {

                            @Subscribe
                            public void lister(DeadEvent event) {
                                log.warn(
                                        "from {} Dead Event : {}",
                                        event.getSource().getClass().getSimpleName(),
                                        event.getEvent());
                            }
                        });
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            beans.putAll(SpringHelper.getContext().getBeansOfType(EventAdapter.class));
            beans.values()
                    .forEach(
                            e -> {
                                EventBusHelper.register(e);
                            });
        }
    }

    @Override
    public void destroy() throws Exception {
        beans.values()
                .forEach(
                        e -> {
                            EventBusHelper.unregister(e);
                        });
        Singleton.remove(EventBusHelper.class.getName());
    }
}
