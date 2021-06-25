package com.xiesx.fastboot.core.signer.annotation;

import java.lang.annotation.*;

/**
 * @title GoSigner.java
 * @description 数据签名
 * @author xiesx
 * @date 2020-7-21 22:35:16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GoSigner {

    boolean ignore() default false;
}
