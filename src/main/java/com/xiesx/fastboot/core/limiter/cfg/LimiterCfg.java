package com.xiesx.fastboot.core.limiter.cfg;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import com.google.common.util.concurrent.RateLimiter;
import com.xiesx.fastboot.core.limiter.LimiterAspect;

/**
 * @title LimiterCfg.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:01:35
 */
@EnableConfigurationProperties(LimiterProperties.class)
// @ConditionalOnProperty(prefix = LimiterProperties.PREFIX, name = "enabled", havingValue = "true",
// matchIfMissing = true)
@ConditionalOnClass({RateLimiter.class})
@Import({LimiterAspect.class})
public class LimiterCfg {
}
