package com.xiesx.fastboot.app.configuration;

import com.xiesx.fastboot.core.advice.annotation.GoEnableBody;
import com.xiesx.fastboot.core.event.annotation.GoEnableEventBus;
import com.xiesx.fastboot.core.exception.annotation.GoEnableException;
import com.xiesx.fastboot.core.json.annotation.GoEnableFastJson;
import com.xiesx.fastboot.core.limiter.annotation.GoEnableLimiter;
import com.xiesx.fastboot.core.logger.annotation.GoEnableLogger;
import com.xiesx.fastboot.core.signature.annotation.GoEnableSigner;
import com.xiesx.fastboot.core.token.annotation.GoEnableToken;

import org.springframework.context.annotation.Configuration;

@Configuration
// 启用统一返回
@GoEnableBody
// 启用事件总线
@GoEnableEventBus
// 启用全局异常
@GoEnableException
// 启用FastJson
@GoEnableFastJson
// 启用请求限流
@GoEnableLimiter
// 启用日志打印
@GoEnableLogger
// 启用数据签名
@GoEnableSigner
// 启用令牌认证
@GoEnableToken
public class FastBootCfg {}
