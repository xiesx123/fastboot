package com.xiesx.fastboot.core.event;

import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("event")
public class EventBusController {

    @GetMapping("post")
    public Result request(String p) {
        // 发布Base消息
        EventBusHelper.post(new SimpleEvent(p, true));
        return R.succ();
    }
}
