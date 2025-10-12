package com.xiesx.fastboot.support.actuator.model.plan;

import cn.hutool.http.Method;

import com.xiesx.fastboot.support.actuator.plans.AbstractPlan;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RequestPlan extends AbstractPlan {

    /** 请求方式 */
    private Method method = Method.GET;

    /** 请求地址 */
    private String url;

    /** 超时时间 */
    private Integer timeout = 5;
}
