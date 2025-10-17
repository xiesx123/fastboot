package com.xiesx.fastboot.core.signature.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GoSigner {

    boolean ignore() default false;
}
