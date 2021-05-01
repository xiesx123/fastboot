package com.xiesx.fastboot.core.limiter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.limiter.annotation.GoLimiter;

/**
 * @title LimiterController.java
 * @description
 * @author xiesx
 * @date 2021-04-05 17:27:35
 */
@RestController
@RequestMapping("/limiter")
public class LimiterController extends BaseController {

    /**
     * 限流
     * 
     * @return
     */
    @GoLimiter(limit = 1, message = "该接口测试每秒限流1个请求")
    @RequestMapping("/limiter")
    public Result limiter() {
        return R.succ();
    }
}
