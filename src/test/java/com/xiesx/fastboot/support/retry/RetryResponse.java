package com.xiesx.fastboot.support.retry;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.base.IStatus;
import com.xiesx.fastboot.base.result.R;
import com.yomahub.tlog.context.TLogContext;

import cn.hutool.core.date.SystemClock;
import lombok.Data;

/**
 * @title RetryResponse.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:21:26
 */
@Data
public class RetryResponse implements IStatus {

    @JSONField(ordinal = 1)
    private Integer code;

    @JSONField(ordinal = 2)
    private String msg;

    @JSONField(ordinal = 3)
    private Object data;

    @JSONField(ordinal = 4)
    public long getTime() {
        return SystemClock.now();
    }

    @JSONField(ordinal = 5)
    public String getTrace() {
        return TLogContext.getTraceId();
    }

    @JSONField(ordinal = 6)
    public boolean getStatus() {
        return isSuccess();
    }

    // =========================

    @Override
    @JSONField(serialize = false)
    public boolean isSuccess() {
        return code == R.SUCCESS_CODE;
    }
}
