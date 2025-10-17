package com.xiesx.fastboot.core.signature.configuration;

import com.xiesx.fastboot.core.signature.SignerAspect;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties(SignerProperties.class)
// @ConditionalOnProperty(prefix = SignerProperties.PREFIX, name = "enabled", havingValue = "true",
// matchIfMissing = true)
@Import({SignerAspect.class})
public class SignerCfg {}
