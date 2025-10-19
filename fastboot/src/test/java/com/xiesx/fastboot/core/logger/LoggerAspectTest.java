package com.xiesx.fastboot.core.logger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import com.xiesx.fastboot.test.base.BaseMock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class LoggerAspectTest extends BaseMock {

    LoggerAspect cls;

    @BeforeEach
    void setup() {
        cls = new LoggerAspect();
    }

    @Test
    @Order(1)
    void testConstructor() {
        cls.loggerPointcut();
        assertNotNull(cls);
    }

    @Test
    @Order(1)
    void testFilterArgs_removesServletRequestAndResponse() {
        Object[] input = {mock(ServletRequest.class), mock(ServletResponse.class), "valid"};
        Object[] result = cls.filterArgs(input);
        assertArrayEquals(new Object[] {"valid"}, result);
    }

    @Test
    @Order(2)
    void testFilterArgs_removesMultipartFileAndModel() {
        Object[] input = {mock(MultipartFile.class), mock(Model.class), 123};
        Object[] result = cls.filterArgs(input);
        assertArrayEquals(new Object[] {123}, result);
    }

    @Test
    @Order(3)
    void testFilterArgs_keepsNonFilteredTypes() {
        Object[] input = {"string", 42, true};
        Object[] result = cls.filterArgs(input);
        assertArrayEquals(input, result);
    }

    @Test
    @Order(4)
    void testFilterArgs_emptyInput_returnsEmpty() {
        Object[] input = {};
        Object[] result = cls.filterArgs(input);
        assertEquals(0, result.length);
    }

    @Test
    @Order(5)
    void testFilterArgs_nullInput_returnsNull() {
        Object[] result = cls.filterArgs(null);
        assertNull(result);
    }
}
