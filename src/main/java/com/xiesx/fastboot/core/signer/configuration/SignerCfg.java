package com.xiesx.fastboot.core.signer.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import com.xiesx.fastboot.core.signer.SignerAspect;

/**
 * @title SignalCfg.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:04:21
 */
@EnableConfigurationProperties(SignerProperties.class)
// @ConditionalOnProperty(prefix = SignerProperties.PREFIX, name = "enabled", havingValue = "true",
// matchIfMissing = true)
@Import({SignerAspect.class})
public class SignerCfg {
}
