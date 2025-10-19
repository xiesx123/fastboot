package com.xiesx.fastboot.core.json;

import com.xiesx.fastboot.core.logger.annotation.GoLogger;
import com.xiesx.fastboot.core.logger.storage.LogStorageProviderSimple;
import com.xiesx.fastboot.test.mock.MockData;
import com.xiesx.fastboot.test.mock.MockUser;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fastjson")
@GoLogger(storage = LogStorageProviderSimple.class)
public class FastJsonController {

    @GetMapping("desensitized")
    public MockUser desensitized() {
        return MockData.user();
    }
}
