package com.xiesx.fastboot.core.eventbus;

import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.logger.LogStorageMysqlProvider;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;

/**
 * @title EventBusController.java
 * @description
 * @author xiesx
 * @date 2021-04-05 17:27:35
 */
@RestController
@RequestMapping("/event")
@GoLogger(storage = LogStorageMysqlProvider.class)
public class EventBusController extends BaseController {

    /**
     * 发布消息
     *
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "request")
    public Result request(String p) throws InterruptedException {
        // 发布Base消息
        EventBusHelper.post(new SimpleEvent(p, true));
        // 发布Object消息
        EventBusHelper.post(p);
        // 模拟耗时操作，线程暂停3秒
        TimeUnit.SECONDS.sleep(5);
        return R.succ();
    }
}
