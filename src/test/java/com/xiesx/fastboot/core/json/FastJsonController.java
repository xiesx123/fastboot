package com.xiesx.fastboot.core.json;

import com.xiesx.fastboot.app.mock.MockData;
import com.xiesx.fastboot.app.mock.MockUser;
import com.xiesx.fastboot.core.logger.LogStorageSimpleProvider;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fastjson")
@GoLogger(storage = LogStorageSimpleProvider.class)
public class FastJsonController {

    @GetMapping("desensitized")
    public MockUser desensitized() {
        return MockData.user();
    }
}
