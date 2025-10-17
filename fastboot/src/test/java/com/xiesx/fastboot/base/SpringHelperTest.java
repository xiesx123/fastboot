package com.xiesx.fastboot.base;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cn.hutool.extra.spring.SpringUtil;

import com.xiesx.fastboot.SpringHelper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

class SpringHelperTest {

    private MockedStatic<SpringUtil> springUtilMock;

    @BeforeEach
    void setUp() {
        springUtilMock = Mockito.mockStatic(SpringUtil.class);
    }

    @AfterEach
    void tearDown() {
        springUtilMock.close();
    }

    @Test
    void testGetUrl() throws Exception {
        String url = SpringHelper.getUrl();
        assertNotNull(url);
    }

    @Test
    void testGetContext() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        springUtilMock.when(SpringUtil::getApplicationContext).thenReturn(mockContext);

        ApplicationContext context = SpringHelper.getContext();
        assertNotNull(context);
        assertEquals(mockContext, context);
    }

    @Test
    void testGetBeanByClass() {
        MyBean mockBean = new MyBean();
        springUtilMock.when(() -> SpringUtil.getBean(MyBean.class)).thenReturn(mockBean);

        MyBean bean = SpringHelper.getBean(MyBean.class);
        assertNotNull(bean);
        assertEquals(mockBean, bean);
    }

    @Test
    void testGetBeanByName() {
        MyBean mockBean = new MyBean();
        springUtilMock.when(() -> SpringUtil.getBean("myBean")).thenReturn(mockBean);

        MyBean bean = SpringHelper.getBean("myBean");
        assertNotNull(bean);
        assertEquals(mockBean, bean);
    }

    @Test
    void testGetBeanByNameAndClass() {
        MyBean mockBean = new MyBean();
        springUtilMock.when(() -> SpringUtil.getBean("myBean", MyBean.class)).thenReturn(mockBean);

        MyBean bean = SpringHelper.getBean("myBean", MyBean.class);
        assertNotNull(bean);
        assertEquals(mockBean, bean);
    }

    @Test
    void testHasBean() {
        MyBean mockBean = new MyBean();
        springUtilMock.when(() -> SpringUtil.getBean(MyBean.class)).thenReturn(mockBean);

        Optional<MyBean> optionalBean = SpringHelper.hasBean(MyBean.class);
        assertTrue(optionalBean.isPresent());
        assertEquals(mockBean, optionalBean.get());
    }

    @Test
    void testHasBeanWhenBeanDoesNotExist() {
        springUtilMock.when(() -> SpringUtil.getBean(MyBean.class)).thenReturn(null);

        Optional<MyBean> optionalBean = SpringHelper.hasBean(MyBean.class);
        assertFalse(optionalBean.isPresent());
    }

    static class MyBean {
        // Dummy class for testing
    }
}
