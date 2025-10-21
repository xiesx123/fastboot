package com.xiesx.fastboot.core.event;

import cn.hutool.core.lang.Singleton;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.xiesx.fastboot.base.config.Configed;
import java.util.concurrent.Executors;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class EventBusHelper {

  private static final EventBus eventbus =
      Singleton.get(
          EventBusHelper.class.getName(),
          () -> new AsyncEventBus(Configed.FASTBOOT, Executors.newCachedThreadPool()));

  /** 获取事件总线 */
  public static EventBus getEventBus() {
    return eventbus;
  }

  /** 注册 */
  public static void register(EventAdapter<?> handler) {
    eventbus.register(handler);
    log.debug("registered event : {}", handler.getClass());
  }

  /** 发布 */
  public static void post(Object object) {
    eventbus.post(object);
  }

  /** 注销 */
  public static void unregister(EventAdapter<?> handler) {
    eventbus.unregister(handler);
    log.debug("unregisted event : {}", handler.getClass());
  }
}
