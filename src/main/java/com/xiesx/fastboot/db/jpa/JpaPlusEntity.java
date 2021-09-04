package com.xiesx.fastboot.db.jpa;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * @title JpaPlusEntity.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:03:38
 */
public abstract class JpaPlusEntity<T> implements Serializable {

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
