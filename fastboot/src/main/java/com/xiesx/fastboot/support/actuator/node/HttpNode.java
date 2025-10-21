package com.xiesx.fastboot.support.actuator.node;

import cn.hutool.http.Method;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HttpNode extends AbstractNode {

  /** 请求方式 */
  private Method method = Method.GET;

  /** 请求地址 */
  private String url;

  /** 超时时间 */
  private Integer timeout = 5;
}
