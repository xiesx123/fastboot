package com.xiesx.fastboot.db.jpa;

import com.xiesx.fastboot.base.result.R;

import java.io.Serializable;

public abstract class JpaPlusEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public String toJSONString() {
        return R.toJsonStr(this);
    }
}
