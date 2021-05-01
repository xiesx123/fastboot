package com.xiesx.fastboot.base.page;

import lombok.Data;

/**
 * @title PageVo.java
 * @description
 * @author xiesx
 * @date 2021-04-06 09:59:33
 */
@Data
public class PageVo {

    public Integer page = 1;

    public Integer limit = 25;

    public Integer size = 25;

    public Integer getPage() {
        return page - 1;
    }
}
