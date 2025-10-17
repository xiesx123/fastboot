package com.xiesx.fastboot.base.page;

import com.alibaba.fastjson2.annotation.JSONField;
import com.google.common.base.Objects;
import com.xiesx.fastboot.base.IStatus;
import com.xiesx.fastboot.base.result.R;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Data
@Builder
@FieldNameConstants(innerTypeName = "FIELDS")
public class PResult implements IStatus {

    /** 状态 */
    @JSONField(ordinal = 1)
    public Integer code;

    /** 消息 */
    @JSONField(ordinal = 2)
    public String msg;

    /** 数据 */
    @JSONField(ordinal = 3)
    public List<?> data;

    /** 总数 */
    @JSONField(ordinal = 4)
    public Integer count;

    /** 当前状态 */
    @JSONField(ordinal = 4)
    public boolean getStatus() {
        return isSuccess();
    }

    /** 判断是否成功 */
    @Override
    @JSONField(serialize = false)
    public boolean isSuccess() {
        return Objects.equal(code, R.SUCCESS_CODE);
    }
}
