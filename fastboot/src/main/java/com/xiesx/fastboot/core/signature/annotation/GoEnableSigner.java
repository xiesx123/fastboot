package com.xiesx.fastboot.core.signature.annotation;

import com.xiesx.fastboot.core.signature.configuration.SignerCfg;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({SignerCfg.class})
@Documented
public @interface GoEnableSigner {}
