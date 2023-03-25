package com.xiesx.fastboot.base.page;

import java.util.List;

import org.springframework.data.domain.Page;

import com.xiesx.fastboot.base.result.R;

import cn.hutool.core.convert.Convert;
import lombok.NonNull;

/**
 * @title PR.java
 * @description
 * @author xiesx
 * @date 2021-04-06 09:58:44
 */
public class PR {

    private static String MSG_EMPTY = "no data";

    public static PResult create(@NonNull Page<?> page) {
        return create(page.toList(), (int) page.getTotalElements());
    }

    public static PResult create(@NonNull List<?> data, Integer total) {
        return PResult.builder().code(R.SUCCESS_CODE).msg(data.isEmpty() ? MSG_EMPTY : R.SUCCESS_MSG).data(data).count(total).build();
    }

    public static PResult create(@NonNull List<?> data, Long total) {
        return PResult.builder().code(R.SUCCESS_CODE).msg(data.isEmpty() ? MSG_EMPTY : R.SUCCESS_MSG).data(data).count(Convert.toInt(total)).build();
    }
}
