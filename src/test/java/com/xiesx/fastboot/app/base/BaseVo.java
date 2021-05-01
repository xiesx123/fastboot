package com.xiesx.fastboot.app.base;

import lombok.Data;

/**
 * @title BaseVo
 * @description
 * @author xiesx
 * @date 2020-4-2410:01:10
 */
@Data
public class BaseVo {

    /** 参数id */
    private Long id = -1L;

    /** 被删除的参数的ID **/
    private Long[] ids;

    /** 搜索词 **/
    private String keyword = "";

    private String key = "";
}
