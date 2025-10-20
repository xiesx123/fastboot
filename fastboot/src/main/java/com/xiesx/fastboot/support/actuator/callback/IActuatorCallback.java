package com.xiesx.fastboot.support.actuator.callback;

import cn.hutool.core.lang.Dict;
import org.jspecify.annotations.Nullable;

/**
 * @title IActuatorCallback.java
 * @description 任务回调
 * @author xiesx
 * @date 2021-07-30 16:49:28
 */
public interface IActuatorCallback<V extends @Nullable Object> {

  void onSuccess(Dict ctx, V result);

  void onFailure(Dict ctx, Throwable t);
}
