package com.xiesx.fastboot.support.actuator.model;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson2.JSON;
import java.util.List;
import lombok.Data;

@Data
public class ActuatorPlan {

  private String id;

  private String title;

  private Dict env;

  private List<JSON> plans;
}
