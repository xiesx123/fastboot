package com.xiesx.fastboot.base.page;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.base.AbstractState;
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
public class PResult implements AbstractState {

    @JSONField(ordinal = 1)
    public Integer code;

    @JSONField(ordinal = 2)
    public String msg;

    @JSONField(ordinal = 3)
    public List<?> data;

    @JSONField(ordinal = 4)
    public Integer count;

    /**
     * 判断是否成功
     */
    @Override
    @JSONField(serialize = false)
    public Boolean isSuccess() {
        return code == R.CODE_SUCCESS ? true : false;
    }
}
