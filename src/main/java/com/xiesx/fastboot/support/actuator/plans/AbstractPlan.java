package com.xiesx.fastboot.support.actuator.plans;

import cn.hutool.core.util.StrUtil;

import com.google.common.collect.Maps;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.Map;

@Data
@Accessors(fluent = true)
@FieldNameConstants(innerTypeName = "FIELDS")
public class AbstractPlan {

    /** 名称 */
    public String name;

    /** 类型 */
    public PlanEnum type;

    /** 请求参数 */
    public Map<String, Object> params = Maps.newConcurrentMap();

    /** 响应节点 */
    public String ret;

    /** 规则 */
    public String rule;

    /** 是否忽略失败 */
    private boolean ignoreFailure = false;

    // ==============

    /** 是否执行规则表达式 */
    public boolean isAviator() {
        return StrUtil.isNotBlank(this.rule);
    }
}
