package com.xiesx.fastboot.support.actuator.callable;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.system.SystemUtil;
import com.xiesx.fastboot.support.actuator.ActuatorContext;
import com.xiesx.fastboot.support.actuator.ActuatorContext.Envar;
import java.util.concurrent.Callable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Log4j2
public class EnvCallable implements Callable<Dict> {

  @NonNull Envar env;

  @Override
  public Dict call() throws Exception {
    log.debug("初始环境");
    // 初始数据
    Dict dict = Dict.create();

    // 环境信息
    Dict temp = Dict.parse(env);
    MapUtil.get(temp, Envar.FIELDS.custom, Dict.class)
        .forEach(
            (k, v) -> {
              temp.set(k, v);
            });
    temp.remove(Envar.FIELDS.custom);
    dict.set(ActuatorContext.FIELDS.env, temp);

    // 系统信息
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
    return dict;
  }
}
