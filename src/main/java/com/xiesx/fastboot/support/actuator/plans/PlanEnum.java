package com.xiesx.fastboot.support.actuator.plans;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @title PlanEnum.java
 * @description
 * @author xiesx
 * @date 2022-11-14 22:16:26
 */
@Getter
@AllArgsConstructor
public enum PlanEnum {

    HTTP("http");

    private String type;

    @Override
    public String toString() {
        return type;
    }

    /**
     * 是否http
     *
     * @return
     */
    public boolean isHttp() {
        return EnumUtil.isEnum(HTTP);
    }
}
