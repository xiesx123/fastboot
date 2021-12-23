package com.xiesx.fastboot.app.configuration;

import org.springframework.context.annotation.Configuration;

import com.xiesx.fastboot.core.body.annotation.GoEnableBody;
import com.xiesx.fastboot.core.eventbus.annotation.GoEnableEventBus;
import com.xiesx.fastboot.core.exception.annotation.GoEnableException;
import com.xiesx.fastboot.core.fastjson.annotation.GoEnableFastJson;
import com.xiesx.fastboot.core.limiter.annotation.GoEnableLimiter;
import com.xiesx.fastboot.core.logger.annotation.GoEnableLogger;
import com.xiesx.fastboot.core.signer.annotation.GoEnableSigner;
import com.xiesx.fastboot.core.token.annotation.GoEnableToken;

/**
 * @title FastBootCfg.java
 * @description
 * @author xiesx
 * @date 2021-04-03 15:58:29
 */
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
public class FastBootCfg {

}
