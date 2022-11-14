package com.xiesx.fastboot.support.actuator.model;

import java.util.Map;

import com.google.common.collect.Maps;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * @title ActuatorEnv.java
 * @description
 * @author xiesx
 * @date 2022-11-12 19:37:27
 */
@Data
@Builder
@FieldNameConstants(innerTypeName = "FIELDS")
public class ActuatorEnv {

    /**
     * 跟踪ID
     */
    @Builder.Default
    private String trace = IdUtil.fastSimpleUUID();


    /**
     * 调试参数
     */
    @Builder.Default
    public boolean debug = false;


    /**
     * 结果保存
     */
    @Builder.Default
    public String saveDir = FileUtil.getTmpDirPath();

    /**
     * 自定义参数
     */
    @Builder.Default
    public Map<String, Object> custom = Maps.newConcurrentMap();
}
