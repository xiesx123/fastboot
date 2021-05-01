package com.xiesx.fastboot.core.token;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.token.annotation.GoHeader;
import com.xiesx.fastboot.core.token.annotation.GoToken;
import com.xiesx.fastboot.core.token.header.HeaderParam;

/**
 * @title BodyController.java
 * @description
 * @author xiesx
 * @date 2021-04-03 15:49:29
 */
@RestController
@RequestMapping("/token")
public class TokenController extends BaseController {

    @RequestMapping(value = "header")
    public Result header(String name, @GoToken String uid) {
        return R.succ(uid);
    }

    @RequestMapping(value = "header2")
    public Result header2(String name, @GoHeader HeaderParam p1, @GoHeader HeaderParamExt p2) {
        return R.succ(p1);
    }

    @RequestMapping(value = "header3")
    public Result header3(String name, @GoToken String uid, @GoHeader HeaderParam p1, @GoHeader HeaderParamExt p2) {
        return R.succ(p2);
    }
}
