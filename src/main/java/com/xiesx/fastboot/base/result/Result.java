package com.xiesx.fastboot.base.result;

import org.jboss.logging.MDC;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.base.IStatus;
import com.xiesx.fastboot.base.config.Configed;

import cn.hutool.core.date.SystemClock;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * @title Result.java
 * @description
 * @author xiesx
 * @date 2021-04-06 09:59:48
 */
@Data
@Accessors(fluent = true)
@FieldNameConstants(innerTypeName = "FIELDS")
public class Result implements IStatus {

    /**
     * 状态
     */
    @JSONField(ordinal = 1)
    public Integer code;

    /**
     * 消息
     */
    @JSONField(ordinal = 2)
    public String msg;

    /**
     * 数据
     */
    @JSONField(ordinal = 3)
    public Object data;

    /**
     * 执行时间
     *
     * @return
     */
    @JSONField(ordinal = 4)
    public long getTime() {
        return SystemClock.now();
    }

    /**
     * trace
     *
     * @return
     */
    @JSONField(ordinal = 5)
    public String getTrace() {
        return (String) MDC.get(Configed.TRACEID);
    }


    /**
     * 当前状态
     *
     * @return
     */
    @JSONField(ordinal = 6)
    public boolean getStatus() {
        return isSuccess();
    }

    // =========================

    /**
     * 判断是否成功
     *
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public boolean isSuccess() {
        return code == R.SUCCESS_CODE;
    }

    /**
     * 判断是否失败
     *
     * @return
     */
    @JSONField(serialize = false)
    public boolean isFail() {
        return code == R.FAIL_CODE;
    }

    /**
     * 判断是否异常
     *
     * @return
     */
    @JSONField(serialize = false)
    public boolean isError() {
        return code == R.ERROR_CODE;
    }

    /**
     * 判断是否重试失败
     *
     * @return
     */
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
