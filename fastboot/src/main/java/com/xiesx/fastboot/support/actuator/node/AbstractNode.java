package com.xiesx.fastboot.support.actuator.node;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Data
@Accessors(fluent = true)
@FieldNameConstants(innerTypeName = "FIELDS")
public class AbstractNode {

  /** 名称 */
  public String name;

  /** 类型 */
  public NodeEnum type;

  /** 请求参数 */
  public Map<String, Object> params = Maps.newLinkedHashMap();

  /** 响应节点 */
  public String ret;

  /** 执行规则（条件成立则执行） */
  public String rule;

  /** 忽略失败 */
  private boolean ignoreFailure = false;

  // ==============

  /** 是否执行规则表达式 */
  public boolean isAviator() {
    return StrUtil.isNotBlank(this.rule);
  }
}
