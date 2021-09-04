package com.xiesx.fastboot.base.page;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Objects;
import com.xiesx.fastboot.base.AbstractStatus;
import com.xiesx.fastboot.base.result.R;

import lombok.Builder;
import lombok.Data;

/**
 * @title PResult.java
 * @description
 * @author xiesx
 * @date 2021-04-06 09:58:30
 */
@Data
@Builder
public class PResult implements AbstractStatus {

    // 返回值
    @JSONField(ordinal = 1)
    public Integer code;

    // 消息
    @JSONField(ordinal = 2)
    public String msg;

    // 数据
    @JSONField(ordinal = 3)
    public List<?> data;

    // 总数
    @JSONField(ordinal = 4)
    public Integer count;

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
}
