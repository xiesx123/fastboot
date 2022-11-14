package com.xiesx.fastboot.support.actuator.plans;

import java.util.Map;

import com.google.common.collect.Maps;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * @title AbstractPlan.java
 * @description
 * @author xiesx
 * @date 2022-11-14 22:13:26
 */
@Data
@Accessors(fluent = true)
@FieldNameConstants(innerTypeName = "FIELDS")
public class AbstractPlan {

    /**
     * 名称
     */
    public String name;

    /**
     * 类型
     */
    public PlanEnum type;

    /**
     * 请求参数
     */
    public Map<String, Object> params = Maps.newConcurrentMap();

    /**
     * 响应节点
     */
    public String ret;

    /**
     * 规则
     */
    public String rule;

    /**
     * 是否忽略失败
     */
    private boolean ignoreFailure = false;

    // ==============

    /**
     * 是否执行规则表达式
     *
     * @return
     */
    public boolean isAviator() {
        return StrUtil.isNotBlank(this.rule);
    }
}
