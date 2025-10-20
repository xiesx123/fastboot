package com.xiesx.fastboot.support.actuator.callback;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.support.actuator.model.ActuatorEnv;
import java.io.File;
import java.nio.charset.Charset;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@RequiredArgsConstructor
public class ActuatorFutureCallback implements IActuatorCallback<Dict> {

  @NonNull ActuatorEnv evn;

  @Override
  public void onSuccess(Dict ctx, Dict result) {
    if (evn.isDebug()) {
      extracted(ctx, result);
    }
    log.info("result callback {}", R.toJsonStr(result));
  }

  @Override
  public void onFailure(Dict ctx, Throwable t) {
    if (evn.isDebug()) {
      extracted(ctx, Dict.create());
    }
    log.error("result callback fail", t);
  }

  /** 保存结果 */
  private void extracted(Dict ctx, Dict result) {
    Dict dict = Dict.create().set("context", ctx).set("result", result);
    File file =
        FileUtil.writeString(
            R.toJsonPrettyStr(dict),
            FileUtil.newFile(StrUtil.format("{}/{}", evn.getSaveDir(), evn.getTrace() + ".json")),
            Charset.defaultCharset());
    log.info("result filepath {}", file.getAbsolutePath());
  }
}
