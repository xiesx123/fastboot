package com.xiesx.fastboot.base.page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.xiesx.fastboot.base.result.R;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class PRTest {

    List<String> sampleData;

    @BeforeEach
    void setup() {
        sampleData = Arrays.asList("A", "B", "C");
    }

    @Test
    void testConstructor() {
        PR cls = new PR();
        assertNotNull(cls);
    }

    // 1. create(Page<?> page)
    @Test
    @Order(1)
    void testCreateFromPage() {
        Page mockPage = Mockito.mock(Page.class);
        when(mockPage.toList()).thenReturn((List) sampleData);
        when(mockPage.getTotalElements()).thenReturn(3L);

        PResult result = PR.create(mockPage);
        assertEquals(0, result.getCode());
        assertEquals(R.SUCCESS_MSG, result.getMsg());
        assertEquals(3, result.getCount());
        assertEquals(sampleData, result.getData());
    }

    // 2. create(List<?> data, Integer total)
    @Test
    @Order(2)
    void testCreateWithIntegerTotal_nonEmpty() {
        PResult result = PR.create(sampleData, 3);
        assertEquals(0, result.getCode());
        assertEquals(R.SUCCESS_MSG, result.getMsg());
        assertEquals(3, result.getCount());
        assertEquals(sampleData, result.getData());
    }

    @Test
    @Order(3)
    void testCreateWithIntegerTotal_emptyList() {
        PResult result = PR.create(Collections.emptyList(), 0);
        assertEquals(0, result.getCode());
        assertEquals(PR.MSG_EMPTY, result.getMsg());
        assertEquals(0, result.getCount());
        assertTrue(result.getData().isEmpty());
    }

    // 3. create(List<?> data, Long total)
    @Test
    @Order(4)
    void testCreateWithLongTotal_nonEmpty() {
        PResult result = PR.create(sampleData, 3L);
        assertEquals(0, result.getCode());
        assertEquals(R.SUCCESS_MSG, result.getMsg());
        assertEquals(3, result.getCount());
        assertEquals(sampleData, result.getData());
    }

    @Test
    @Order(5)
    void testCreateWithLongTotal_emptyList() {
        PResult result = PR.create(Collections.emptyList(), 0L);
        assertEquals(0, result.getCode());
        assertEquals(PR.MSG_EMPTY, result.getMsg());
        assertEquals(0, result.getCount());
        assertTrue(result.getData().isEmpty());
    }
}
