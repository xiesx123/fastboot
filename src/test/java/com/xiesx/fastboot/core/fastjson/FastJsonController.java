package com.xiesx.fastboot.core.fastjson;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.app.mock.MockData;
import com.xiesx.fastboot.app.mock.MockUser;
import com.xiesx.fastboot.app.mock.MockUserDesensitized;
import com.xiesx.fastboot.core.logger.LogStorageMysqlProvider;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;

/**
 * @title FastJsonController.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:13
 */
@RestController
@RequestMapping("/fastjson")
@GoLogger(storage = LogStorageMysqlProvider.class)
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
    @RequestMapping("/json/desensitized")
    public MockUserDesensitized desensitized() {
        return MockData.userDesensitized();
    }
}
