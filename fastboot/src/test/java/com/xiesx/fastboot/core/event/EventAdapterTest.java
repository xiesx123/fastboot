package com.xiesx.fastboot.core.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;

import com.xiesx.fastboot.test.base.BaseMock;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.lang.reflect.ParameterizedType;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class EventAdapterTest extends BaseMock {

    @Mock Logger mockLogger;

    @Spy TestEventAdapter adapter = new TestEventAdapter();

    static class TestEvent {}

    static class TestEventAdapter extends EventAdapter<TestEvent> {
        Logger injectedLogger;

        void injectLogger(Logger logger) {
            this.injectedLogger = logger;
        }

        @Override
        public boolean process(TestEvent t) throws Exception {
            return true;
        }

        @Override
        public void onEvent(TestEvent event) {
            if (ObjectUtil.isNotNull(
                    ReflectUtil.getMethod(this.getClass(), "process", event.getClass()))) {
                try {
                    ParameterizedType p =
                            (ParameterizedType) this.getClass().getGenericSuperclass();
                    Class<TestEvent> c = (Class<TestEvent>) p.getActualTypeArguments()[0];
                    if (c.getName().equals(event.getClass().getName()) && !process(event)) {
                        injectedLogger.warn("handle event {} fail", event.getClass());
                    }
                } catch (Exception e) {
                    injectedLogger.error("handle event {} {} ", event.getClass(), e.getMessage());
                }
            }
        }
    }

    @BeforeEach
    void setup() {
        adapter.injectLogger(mockLogger);
    }

    @Test
    @Order(1)
    void testOnEvent_processReturnsTrue_noLog() {
        TestEvent event = new TestEvent();

        adapter.onEvent(event);

        verify(mockLogger, never()).warn(anyString(), any(), any());
        verify(mockLogger, never()).error(anyString(), any(), any());
    }

    @Test
    @Order(2)
    void testOnEvent_processReturnsFalse_logsWarning() throws Exception {
        TestEvent event = new TestEvent();

        doReturn(false).when(adapter).process(any());

        adapter.onEvent(event);

        verify(mockLogger).warn("handle event {} fail", TestEvent.class);
    }

    @Test
    @Order(3)
    void testOnEvent_processThrowsException_logsError() throws Exception {
        TestEvent event = new TestEvent();

        doThrow(new RuntimeException("boom")).when(adapter).process(any());

        adapter.onEvent(event);

        verify(mockLogger).error(eq("handle event {} {} "), eq(TestEvent.class), eq("boom"));
    }
}
