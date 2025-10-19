package com.xiesx.fastboot.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import cn.hutool.extra.spring.SpringUtil;

import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.test.base.BaseMock;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class SpringHelperTest extends BaseMock {

    @Test
    void testConstructor() {
        SpringHelper cls = new SpringHelper();
        assertNotNull(cls);
    }

    // 1. getUrl()
    @Test
    @Order(1)
    void testGetUrl() throws Exception {
        String url = SpringHelper.getUrl();
        assertNotNull(url);
    }

    // 2. getContext()
    @Test
    @Order(2)
    void testGetContext() {
        try (MockedStatic<SpringUtil> springMock = mockStatic(SpringUtil.class)) {
            springMock.when(SpringUtil::getApplicationContext).thenReturn(mockContext);
            assertEquals(mockContext, SpringHelper.getContext());
        }
    }

    // 3. getBean(Class<T>)
    @Test
    @Order(3)
    void testGetBeanByClass() {
        String mockBean = "helloBean";
        try (MockedStatic<SpringUtil> springMock = mockStatic(SpringUtil.class)) {
            springMock.when(() -> SpringUtil.getBean(String.class)).thenReturn(mockBean);
            assertEquals("helloBean", SpringHelper.getBean(String.class));
        }
    }

    // 4. getBean(String)
    @Test
    @Order(4)
    void testGetBeanByName() {
        Object mockBean = new Object();
        try (MockedStatic<SpringUtil> springMock = mockStatic(SpringUtil.class)) {
            springMock.when(() -> SpringUtil.getBean("myBean")).thenReturn(mockBean);
            assertEquals(mockBean, SpringHelper.getBean("myBean"));
        }
    }

    // 5. getBean(String, Class<T>)
    @Test
    @Order(5)
    void testGetBeanByNameAndClass() {
        Integer mockBean = 42;
        try (MockedStatic<SpringUtil> springMock = mockStatic(SpringUtil.class)) {
            springMock
                    .when(() -> SpringUtil.getBean("intBean", Integer.class))
                    .thenReturn(mockBean);
            assertEquals(42, SpringHelper.getBean("intBean", Integer.class));
        }
    }

    // 6. hasBean(Class<T>)
    @Test
    @Order(6)
    void testHasBeanPresent() {
        String mockBean = "presentBean";
        try (MockedStatic<SpringUtil> springMock = mockStatic(SpringUtil.class)) {
            springMock.when(() -> SpringUtil.getBean(String.class)).thenReturn(mockBean);
            Optional<String> result = SpringHelper.hasBean(String.class);
            assertTrue(result.isPresent());
            assertEquals("presentBean", result.get());
        }
    }

    @Test
    @Order(7)
    void testHasBeanAbsent() {
        try (MockedStatic<SpringUtil> springMock = mockStatic(SpringUtil.class)) {
            springMock.when(() -> SpringUtil.getBean(R.class)).thenReturn(null);
            Optional<R> result = SpringHelper.hasBean(R.class);
            assertFalse(result.isPresent());
        }
    }
}
