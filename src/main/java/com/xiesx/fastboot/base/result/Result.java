package com.xiesx.fastboot.base.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Objects;
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

    // 返回值
    @JSONField(ordinal = 1)
    public Integer code;

    // 消息
    @JSONField(ordinal = 2)
    public String msg;

    // 数据
    @JSONField(ordinal = 3)
    public Object data;

    // 状态
    @JSONField(ordinal = 4)
    public Boolean getStatus() {
        return isSuccess();
    }

    /**
     * 判断是否成功
     */
    @Override
    @JSONField(serialize = false)
    public Boolean isSuccess() {
        return Objects.equal(code, R.CODE_SUCCESS);
    }

    /**
     * 判断是否失败
     *
     * @return
     */
    @JSONField(serialize = false)
    public Boolean isFail() {
        return Objects.equal(code, R.CODE_FAIL);
    }

    /**
     * 判断是否异常
     *
     * @return
     */
    @JSONField(serialize = false)
    public Boolean isError() {
        return Objects.equal(code, R.CODE_ERROR);
    }

    /**
     * 判断是否重试失败
     *
     * @return
     */
    @JSONField(serialize = false)
    public Boolean isReTry() {
        return Objects.equal(code, R.CODE_RETRY);
    }
}
