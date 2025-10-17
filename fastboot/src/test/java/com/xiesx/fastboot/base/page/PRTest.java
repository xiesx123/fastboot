package com.xiesx.fastboot.base.page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import cn.hutool.core.convert.Convert;

import com.xiesx.fastboot.base.result.R;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PRTest {

    @Test
    void testCreateWithPage() {
        // Mock a Page object
        Page<?> mockPage = Mockito.mock(Page.class);
        when(mockPage.toList()).thenReturn((List) Arrays.asList("item1", "item2"));
        when(mockPage.getTotalElements()).thenReturn(2L);

        // Call the method
        PResult result = PR.create(mockPage);

        // Assertions
        assertEquals(R.SUCCESS_CODE, result.getCode());
        assertEquals(R.SUCCESS_MSG, result.getMsg());
        assertEquals(Arrays.asList("item1", "item2"), result.getData());
        assertEquals(2, result.getCount());
    }

    @Test
    void testCreateWithEmptyPage() {
        // Mock an empty Page object
        Page<?> mockPage = Mockito.mock(Page.class);
        when(mockPage.toList()).thenReturn(Collections.emptyList());
        when(mockPage.getTotalElements()).thenReturn(0L);

        // Call the method
        PResult result = PR.create(mockPage);

        // Assertions
        assertEquals(R.SUCCESS_CODE, result.getCode());
        assertEquals("no data", result.getMsg());
        assertTrue(result.getData().isEmpty());
        assertEquals(0, result.getCount());
    }

    @Test
    void testCreateWithListAndIntegerTotal() {
        // Prepare data
        List<String> data = Arrays.asList("item1", "item2");
        int total = 2;

        // Call the method
        PResult result = PR.create(data, total);

        // Assertions
        assertEquals(R.SUCCESS_CODE, result.getCode());
        assertEquals(R.SUCCESS_MSG, result.getMsg());
        assertEquals(data, result.getData());
        assertEquals(total, result.getCount());
    }

    @Test
    void testCreateWithEmptyListAndIntegerTotal() {
        // Prepare data
        List<String> data = Collections.emptyList();
        int total = 0;

        // Call the method
        PResult result = PR.create(data, total);

        // Assertions
        assertEquals(R.SUCCESS_CODE, result.getCode());
        assertEquals("no data", result.getMsg());
        assertTrue(result.getData().isEmpty());
        assertEquals(total, result.getCount());
    }

    @Test
    void testCreateWithListAndLongTotal() {
        // Prepare data
        List<String> data = Arrays.asList("item1", "item2");
        long total = 2L;

        // Call the method
        PResult result = PR.create(data, total);

        // Assertions
        assertEquals(R.SUCCESS_CODE, result.getCode());
        assertEquals(R.SUCCESS_MSG, result.getMsg());
        assertEquals(data, result.getData());
        assertEquals(Convert.toInt(total), result.getCount());
    }

    @Test
    void testCreateWithEmptyListAndLongTotal() {
        // Prepare data
        List<String> data = Collections.emptyList();
        long total = 0L;

        // Call the method
        PResult result = PR.create(data, total);

        // Assertions
        assertEquals(R.SUCCESS_CODE, result.getCode());
        assertEquals("no data", result.getMsg());
        assertTrue(result.getData().isEmpty());
        assertEquals(Convert.toInt(total), result.getCount());
    }
}
