package com.xiesx.fastboot.core.limiter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.limiter.annotation.GoLimiter;
import com.xiesx.fastboot.core.logger.LogStorageMysqlProvider;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;

import cn.hutool.core.date.DateUtil;

/**
 * @title LimiterController.java
 * @description
 * @author xiesx
 * @date 2021-04-05 17:27:35
 */
@RestController
@RequestMapping("/limiter")
@GoLogger(storage = LogStorageMysqlProvider.class)
public class LimiterController extends BaseController {

    /**
     * 限流
     * 
     * @return
     */
    @GoLimiter(limit = 1, message = "该接口测试每秒内限流1个请求")
    @RequestMapping("/limit")
    public Result limit() {
        return R.succ(DateUtil.now());
    }
}
