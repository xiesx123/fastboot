package com.xiesx.fastboot.core.token;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.logger.LogStorageSimpleProvider;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;
import com.xiesx.fastboot.core.token.annotation.GoHeader;
import com.xiesx.fastboot.core.token.annotation.GoToken;
import com.xiesx.fastboot.core.token.header.HttpHeaderParams;

/**
 * @title TokenController.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:47
 */
@RestController
@RequestMapping("/token")
@GoLogger(storage = LogStorageSimpleProvider.class)
public class TokenController extends BaseController {

    @RequestMapping(value = "header")
    public Result header(String name, @GoToken String uid, @GoHeader HttpHeaderParams p1) {
        return R.succ(Lists.newArrayList(name, uid, p1));
    }
}
