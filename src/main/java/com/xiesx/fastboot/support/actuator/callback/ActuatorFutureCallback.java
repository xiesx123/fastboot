package com.xiesx.fastboot.support.actuator.callback;

import java.io.File;
import java.nio.charset.Charset;

import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.support.actuator.model.ActuatorEnv;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @title ActuatorFutureCallback.java
 * @description
 * @author xiesx
 * @date 2021-12-15 19:00:29
 */
@Log4j2
@Data
@RequiredArgsConstructor
public class ActuatorFutureCallback implements IActuatorCallback<Dict> {

    @NonNull
    ActuatorEnv evn;

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

    /**
     * 保存结果
     *
     * @param ctx
     * @param result
     */
    private void extracted(Dict ctx, Dict result) {
        Dict dict = Dict.create().set("context", ctx).set("result", result);
        File file = FileUtil.writeString(R.toJsonPrettyStr(dict), FileUtil.newFile(StrUtil.format("{}/{}", evn.getSaveDir(), evn.getTrace() + ".json")), Charset.defaultCharset());
        log.info("result filepath {}", file.getAbsolutePath());
    }
}
