package com.xiesx.fastboot.base.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.base.AbstractStatus;

import cn.hutool.core.date.DateUtil;
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
public class Result implements AbstractStatus, AutoCloseable {

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
        return DateUtil.current();
    }

    /**
     * 当前状态
     *
     * @return
     */
    @JSONField(ordinal = 5)
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
        return code == R.CODE_SUCCESS;
    }

    /**
     * 判断是否失败
     *
     * @return
     */
    @JSONField(serialize = false)
    public boolean isFail() {
        return code == R.CODE_FAIL;
    }

    /**
     * 判断是否异常
     *
     * @return
     */
    @JSONField(serialize = false)
    public boolean isError() {
        return code == R.CODE_ERROR;
    }

    /**
     * 判断是否重试失败
     *
     * @return
     */
    @JSONField(serialize = false)
    public boolean isReTry() {
        return code == R.CODE_RETRY;
    }

    // =========================

    public String toJsonString() {
        return R.toJsonStr(this);
    }

    public String toJsonPrettyStr() {
        return R.toJsonPrettyStr(this);
    }

    // =========================

    @Override
    public void close() throws Exception {
    }
}
