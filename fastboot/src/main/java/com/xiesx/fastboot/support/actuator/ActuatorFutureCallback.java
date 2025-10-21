package com.xiesx.fastboot.support.actuator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.support.actuator.ActuatorContext.Envar;
import java.io.File;
import java.nio.charset.Charset;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
public class ActuatorFutureCallback implements IActuatorCallback<Dict> {

  @Override
  public void onSuccess(ActuatorContext ctx, Dict result) {
    Envar evn = ctx.getEnv();
    if (evn.isDebug()) {
      extracted(ctx, result);
    }
    log.info("result callback {}", R.toJsonStr(result));
  }

  @Override
  public void onFailure(ActuatorContext ctx, Throwable t) {
    Envar evn = ctx.getEnv();
    if (evn.isDebug()) {
      extracted(ctx, Dict.create());
    }
    log.error("result callback fail", t);
  }

  /** 保存结果 */
  private void extracted(ActuatorContext ctx, Dict result) {
    Envar evn = ctx.getEnv();
    ctx.result(result);
    File file =
        FileUtil.writeString(
            R.toJsonPrettyStr(ctx),
            FileUtil.newFile(StrUtil.format("{}/{}", evn.getSaveDir(), "test.json")),
            Charset.defaultCharset());
    log.info("result filepath {}", file.getAbsolutePath());
  }
}
