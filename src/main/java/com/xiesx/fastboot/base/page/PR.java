package com.xiesx.fastboot.base.page;

import java.util.List;

import org.springframework.data.domain.Page;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.base.result.R;

import lombok.NonNull;

/**
 * @title PR.java
 * @description
 * @author xiesx
 * @date 2021-04-06 09:58:44
 */
public class PR {

    private static String MSG_EMPTY = "无数据";

    public static PResult create(Page<?> page) {
        return create(page.toList(), (int) page.getTotalElements());
    }

    public static PResult create(@NonNull List<?> data) {
        return create(data, data.size());
    }

    /**
     * 构造
     *
     * @param data
     * @param total
     * @return
     */
    public static PResult create(@NonNull List<?> data, Integer total) {
        // 分页数据
        List<?> list = Lists.newArrayList(data);
        // 返回结果
        return PResult.builder().code(R.CODE_SUCCESS).msg(list.isEmpty() ? MSG_EMPTY : "操作成功").data(list).count(total).build();
    }
}
