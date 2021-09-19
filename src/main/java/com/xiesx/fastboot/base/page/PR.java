package com.xiesx.fastboot.base.page;

import java.util.List;

import org.springframework.data.domain.Page;

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

    public static PResult create(@NonNull Page<?> page) {
        return create(page.toList(), (int) page.getTotalElements());
    }

    public static PResult create(@NonNull List<?> data, Integer total) {
        return PResult.builder().code(R.CODE_SUCCESS).msg(data.isEmpty() ? MSG_EMPTY : "操作成功").data(data).count(total).build();
    }
}
