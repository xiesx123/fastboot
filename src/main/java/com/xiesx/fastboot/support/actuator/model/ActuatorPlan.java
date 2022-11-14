package com.xiesx.fastboot.support.actuator.model;

import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.lang.Dict;
import lombok.Data;

/**
 * @title ActuatorPlan.java
 * @description
 * @author xiesx
 * @date 2021-08-02 10:29:48
 */
@Data
public class ActuatorPlan {

    private String id;

    private String title;

    private Dict env;

    private List<JSON> plans;
}
