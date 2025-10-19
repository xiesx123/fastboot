package com.xiesx.fastboot.core.event.configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cn.hutool.core.lang.Singleton;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.event.EventAdapter;
import com.xiesx.fastboot.core.event.EventBusHelper;
import com.xiesx.fastboot.test.base.BaseMock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class EventBusCfgTest extends BaseMock {

    @Mock EventBus mockEventBus;

    @Mock EventAdapter mockAdapter;

    MockedStatic<EventBusHelper> eventBusHelperMock;

    MockedStatic<SpringHelper> springHelperMock;

    MockedStatic<Singleton> singletonMock;

    @BeforeEach
    void setup() {
        eventBusHelperMock = mockStatic(EventBusHelper.class);
        springHelperMock = mockStatic(SpringHelper.class);
        singletonMock = mockStatic(Singleton.class);

        eventBusHelperMock.when(EventBusHelper::getEventBus).thenReturn(mockEventBus);
        eventBusHelperMock.when(() -> EventBusHelper.register(any())).thenAnswer(inv -> null);
        eventBusHelperMock.when(() -> EventBusHelper.unregister(any())).thenAnswer(inv -> null);
        springHelperMock.when(SpringHelper::getContext).thenReturn(mockContext);
    }

    @AfterEach
    void tearDown() {
        eventBusHelperMock.close();
        springHelperMock.close();
        singletonMock.close();
    }

    @Test
    @Order(1)
    void testConstructor_registersDeadEventListener() {
        new EventBusCfg();
        verify(mockEventBus).register(any());
    }

    @Test
    @Order(3)
    void testOnApplicationEvent_registersEventAdapters() {
        when(mockEvent.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getParent()).thenReturn(null);
        when(mockContext.getBeansOfType(EventAdapter.class))
                .thenReturn(ImmutableMap.of("adapter", mockAdapter));

        EventBusCfg cfg = new EventBusCfg();
        cfg.onApplicationEvent(mockEvent);

        verify(mockContext).getBeansOfType(EventAdapter.class);
        eventBusHelperMock.verify(() -> EventBusHelper.register(mockAdapter));
    }

    @Test
    @Order(4)
    void testDestroy_unregistersAdaptersAndRemovesSingleton() throws Exception {
        when(mockEvent.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getParent()).thenReturn(null);
        when(mockContext.getBeansOfType(EventAdapter.class))
                .thenReturn(ImmutableMap.of("adapter", mockAdapter));

        EventBusCfg cfg = new EventBusCfg();
        cfg.onApplicationEvent(mockEvent);

        singletonMock
                .when(() -> Singleton.remove(EventBusHelper.class.getName()))
                .thenAnswer(inv -> null);

        cfg.destroy();

        eventBusHelperMock.verify(() -> EventBusHelper.unregister(mockAdapter));
        singletonMock.verify(() -> Singleton.remove(EventBusHelper.class.getName()));
    }
}
