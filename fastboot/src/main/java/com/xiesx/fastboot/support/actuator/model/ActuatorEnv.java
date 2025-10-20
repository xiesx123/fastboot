package com.xiesx.fastboot.support.actuator.model;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@Builder
@FieldNameConstants(innerTypeName = "FIELDS")
public class ActuatorEnv {

  /** 跟踪ID */
  @Builder.Default private String trace = IdUtil.fastSimpleUUID();

  /** 调试参数 */
  @Builder.Default public boolean debug = false;

  /** 结果保存 */
  @Builder.Default public String saveDir = FileUtil.getTmpDirPath();

  /** 自定义参数 */
  @Builder.Default public Map<String, Object> custom = Maps.newConcurrentMap();
}
