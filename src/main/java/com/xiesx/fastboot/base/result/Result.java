package com.xiesx.fastboot.base.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.base.AbstractStatus;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * @title Result.java
 * @description
 * @author xiesx
 * @date 2021-04-06 09:59:48
 */
@Data
@Builder
@FieldNameConstants(innerTypeName = "FIELDS")
public class Result implements AbstractStatus {

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
     * 当前状态
     *
     * @return
     */
    @JSONField(ordinal = 4)
    public Boolean getStatus() {
        return isSuccess();
    }

    /**
     * 判断是否成功
     *
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public Boolean isSuccess() {
        return code == R.CODE_SUCCESS;
    }

    /**
     * 判断是否失败
     *
     * @return
     */
    @JSONField(serialize = false)
    public Boolean isFail() {
        return code == R.CODE_FAIL;
    }

    /**
     * 判断是否异常
     *
     * @return
     */
    @JSONField(serialize = false)
    public Boolean isError() {
        return code == R.CODE_ERROR;
    }

    /**
     * 判断是否重试失败
     *
     * @return
     */
    @JSONField(serialize = false)
    public Boolean isReTry() {
        return code == R.CODE_RETRY;
    }
}
