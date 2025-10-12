package com.xiesx.fastboot.base.page;

import cn.hutool.core.convert.Convert;

import com.xiesx.fastboot.base.result.R;

import lombok.NonNull;

import org.springframework.data.domain.Page;

import java.util.List;

public class PR {

    private static String MSG_EMPTY = "no data";

    public static PResult create(@NonNull Page<?> page) {
        return create(page.toList(), (int) page.getTotalElements());
    }

    public static PResult create(@NonNull List<?> data, Integer total) {
        return PResult.builder()
                .code(R.SUCCESS_CODE)
                .msg(data.isEmpty() ? MSG_EMPTY : R.SUCCESS_MSG)
                .data(data)
                .count(total)
                .build();
    }

    public static PResult create(@NonNull List<?> data, Long total) {
        return PResult.builder()
                .code(R.SUCCESS_CODE)
                .msg(data.isEmpty() ? MSG_EMPTY : R.SUCCESS_MSG)
                .data(data)
                .count(Convert.toInt(total))
                .build();
    }
}
