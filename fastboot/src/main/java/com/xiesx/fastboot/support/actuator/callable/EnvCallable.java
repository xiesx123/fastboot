package com.xiesx.fastboot.support.actuator.callable;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.system.SystemUtil;
import com.xiesx.fastboot.support.actuator.model.ActuatorEnv;
import java.util.concurrent.Callable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class EnvCallable implements Callable<Dict> {

  @NonNull ActuatorEnv env;

  @Override
  public Dict call() throws Exception {
    log.debug("初始环境");
    // 初始数据
    Dict dict = Dict.create();
    dict.set(
        "system",
        Dict.create() //
            .set("os", SystemUtil.getOsInfo().getName()) // 系统信息
            .set("user", SystemUtil.getUserInfo().getName()) // 用户信息
            .set("host", SystemUtil.getHostInfo().getName()) // host信息
            .set("java", SystemUtil.getJavaInfo().getVersion()) // java信息
            .set("runtime", SystemUtil.getRuntimeInfo().getTotalMemory()) // java信息
            .set("datatime", DateUtil.now()) //
        );
    // 环境数据
    if (ObjectUtil.isNotEmpty(env)) {
      Dict temp = Dict.parse(env);
      MapUtil.get(temp, ActuatorEnv.FIELDS.custom, Dict.class)
          .forEach(
              (k, v) -> {
                temp.set(k, v);
              });
      temp.remove(ActuatorEnv.FIELDS.custom);
      dict.set("env", temp);
    }
    return dict;
  }
}
