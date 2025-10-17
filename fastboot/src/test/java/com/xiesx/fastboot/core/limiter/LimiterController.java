package com.xiesx.fastboot.core.limiter;

import cn.hutool.core.date.DateUtil;

import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.limiter.annotation.GoLimiter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("limiter")
public class LimiterController {

    @GoLimiter(limit = 1, message = "该接口测试每秒内限流1个请求")
    @GetMapping("limit")
    public Result limit() {
        return R.succ(DateUtil.now());
    }
}
