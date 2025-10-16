package com.xiesx.fastboot.test.base;

import cn.hutool.core.util.StrUtil;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class BaseVo {

    /** 参数id */
    private Long id = -1L;

    /** 被删除的参数的ID * */
    private Long[] ids;

    /** 搜索词 * */
    private String keyword = StrUtil.EMPTY;

    @NotBlank private String key;
}
