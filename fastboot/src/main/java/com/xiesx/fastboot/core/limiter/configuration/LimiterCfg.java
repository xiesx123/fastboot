package com.xiesx.fastboot.core.limiter.configuration;

import com.google.common.util.concurrent.RateLimiter;
import com.xiesx.fastboot.core.limiter.LimiterAspect;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

// @EnableConfigurationProperties(LimiterProperties.class)
// @ConditionalOnProperty(prefix = LimiterProperties.PREFIX, name = "enabled", havingValue = "true",
// matchIfMissing = true)
@ConditionalOnClass({RateLimiter.class})
@Import({LimiterAspect.class})
public class LimiterCfg {}
