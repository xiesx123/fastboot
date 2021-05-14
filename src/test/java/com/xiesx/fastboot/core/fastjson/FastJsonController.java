package com.xiesx.fastboot.core.fastjson;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.app.mock.MockData;
import com.xiesx.fastboot.app.mock.MockDesensitized;
import com.xiesx.fastboot.app.mock.MockUser;

/**
 * @title LimiterController.java
 * @description
 * @author xiesx
 * @date 2021-04-05 17:27:35
 */
@RestController
@RequestMapping("/fastjson")
public class FastJsonController extends BaseController {

    /**
     * 序列化
     * 
     * @return
     */
    @RequestMapping("/json")
    public MockUser json() {
        return MockData.user();
    }

    /**
     * 脱敏
     * 
     * @return
     */
    @RequestMapping("/desensitized")
    public MockDesensitized desensitized() {
        return MockData.user2();
    }
}
