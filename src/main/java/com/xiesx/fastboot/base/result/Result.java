package com.xiesx.fastboot.base.result;

import com.alibaba.fastjson2.annotation.JSONField;
import com.xiesx.fastboot.base.IStatus;
import com.yomahub.tlog.context.TLogContext;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Data
@Accessors(fluent = true)
@FieldNameConstants(innerTypeName = "FIELDS")
public class Result implements IStatus {

    /** 状态 */
    @JSONField(ordinal = 1)
    public Integer code;

    /** 消息 */
    @JSONField(ordinal = 2)
    public String msg;

    /** 数据 */
    @JSONField(ordinal = 3)
    public Object data;

    /** 链路跟踪 */
    @JSONField(ordinal = 5)
    public String getTrace() {
        return TLogContext.getTraceId();
    }

    /** 当前状态 */
    @JSONField(ordinal = 6)
    public boolean getStatus() {
        return isSuccess();
    }

    // =========================

    /** 判断是否成功 */
    @Override
    @JSONField(serialize = false)
    public boolean isSuccess() {
        return code == R.SUCCESS_CODE;
    }

    /** 判断是否失败 */
    @JSONField(serialize = false)
    public boolean isFail() {
        return code == R.FAIL_CODE;
    }

    /** 判断是否异常 */
    @JSONField(serialize = false)
    public boolean isError() {
        return code == R.ERROR_CODE;
    }

    /** 判断是否重试失败 */
    @JSONField(serialize = false)
    public boolean isReTry() {
        return code == R.RETRY_CODE;
    }

    // =========================

    public String toJsonString() {
        return R.toJsonStr(this);
    }

    public String toJsonPrettyStr() {
        return R.toJsonPrettyStr(this);
    }
}
