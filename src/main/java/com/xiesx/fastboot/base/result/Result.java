package com.xiesx.fastboot.base.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.base.AbstractState;

import lombok.Builder;
import lombok.Data;

/**
 * @title Result.java
 * @description
 * @author xiesx
 * @date 2021-04-06 09:59:48
 */
@Data
@Builder
public class Result implements AbstractState {

    // 状态
    @JSONField(ordinal = 1)
    public Integer code;

    // 提示
    @JSONField(ordinal = 2)
    public String msg;

    // 数据
    @JSONField(ordinal = 3)
    public Object data;

    // 状态
    @JSONField(ordinal = 4)
    public Boolean getStatus() {
        return code == R.CODE_SUCCESS;
    }

    /**
     * 判断是否成功
     */
    @Override
    @JSONField(serialize = false)
    public Boolean isSuccess() {
        return code == R.CODE_SUCCESS ? true : false;
    }

    /**
     * 判断是否失败
     *
     * @return
     */
    @JSONField(serialize = false)
    public Boolean isFail() {
        return code == R.CODE_FAIL ? true : false;
    }

    /**
     * 判断是否异常
     *
     * @return
     */
    @JSONField(serialize = false)
    public Boolean isError() {
        return code == R.CODE_ERROR ? true : false;
    }

    /**
     * 判断是否重试失败
     *
     * @return
     */
    @JSONField(serialize = false)
    public Boolean isReTrY() {
        return code == R.CODE_RETRY ? true : false;
    }
}
