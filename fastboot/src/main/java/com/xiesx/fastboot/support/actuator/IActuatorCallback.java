package com.xiesx.fastboot.support.actuator;

import org.jspecify.annotations.Nullable;

public interface IActuatorCallback<V extends @Nullable Object> {

  void onSuccess(ActuatorContext ctx, V result);

  void onFailure(ActuatorContext ctx, Throwable t);
}
