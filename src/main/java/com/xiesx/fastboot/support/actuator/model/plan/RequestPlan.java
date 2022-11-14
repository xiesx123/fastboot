package com.xiesx.fastboot.support.actuator.model.plan;

import com.xiesx.fastboot.support.actuator.plans.AbstractPlan;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.dongliu.requests.Methods;

/**
 * @title HttpPlan.java
 * @description
 * @author xiesx
 * @date 2022-11-14 22:11:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RequestPlan extends AbstractPlan {

    /**
     * 请求方式
     */
    private String method = Methods.GET;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 超时时间
     */
    private Integer timeout = 5;
}
