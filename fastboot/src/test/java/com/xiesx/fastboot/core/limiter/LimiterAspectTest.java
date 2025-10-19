package com.xiesx.fastboot.core.limiter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import cn.hutool.core.annotation.AnnotationUtil;

import com.google.common.util.concurrent.RateLimiter;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import com.xiesx.fastboot.core.limiter.annotation.GoLimiter;
import com.xiesx.fastboot.test.base.BaseMock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class LimiterAspectTest extends BaseMock {

    @Mock GoLimiter mockLimiter;

    @Spy RateLimiter mockRateLimiter = RateLimiter.create(1000);

    LimiterAspect cls;

    @BeforeEach
    void setup() throws Exception {
        cls = new LimiterAspect();
    }

    @Test
    @Order(1)
    void testConstructor() {
        cls.limiterPointcut();
        assertNotNull(cls);
    }

    @Test
    @Order(2)
    void testAroundProceedSuccess() throws Throwable {
        when(mockPjp.getSignature()).thenReturn(mockSignature);
        when(mockSignature.getMethod()).thenReturn(mockMethod);
        when(mockPjp.proceed()).thenReturn("success");

        when(AnnotationUtil.getAnnotation(mockMethod, GoLimiter.class)).thenReturn(mockLimiter);
        when(mockLimiter.limit()).thenReturn(1000.0);
        when(mockRateLimiter.tryAcquire()).thenReturn(true);

        Object result = cls.around(mockPjp);
        assertEquals("success", result);
    }

    @Test
    @Order(3)
    void testAroundThrowsRunExceptionWithMessage() {
        when(mockPjp.getSignature()).thenReturn(mockSignature);
        when(mockSignature.getMethod()).thenReturn(mockMethod);

        when(AnnotationUtil.getAnnotation(mockMethod, GoLimiter.class)).thenReturn(mockLimiter);
        when(mockLimiter.limit()).thenReturn(1000.0);
        when(mockLimiter.message()).thenReturn("Too many requests");
        when(mockRateLimiter.tryAcquire()).thenReturn(false);

        RunException ex = assertThrows(RunException.class, () -> cls.around(mockPjp));
        assertEquals(RunExc.LIMITER, ex.getStatus());
        assertEquals("Too many requests", ex.getMessage());
    }
}
