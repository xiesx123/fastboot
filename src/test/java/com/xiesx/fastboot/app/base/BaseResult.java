package com.xiesx.fastboot.app.base;

import java.io.Serializable;

import com.google.common.base.Objects;
import com.xiesx.fastboot.base.AbstractState;
import com.xiesx.fastboot.base.result.R;

import lombok.Data;

@Data
public class BaseResult<T> implements AbstractState, Serializable {

    private static final long serialVersionUID = 1L;

    public Integer code;

    public String msg;

    public T data;

    @Override
    public Boolean isSuccess() {
        return Objects.equal(code, R.CODE_SUCCESS);
    }
}
