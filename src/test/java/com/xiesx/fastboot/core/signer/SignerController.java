package com.xiesx.fastboot.core.signer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.logger.LogStorageSimpleProvider;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;
import com.xiesx.fastboot.core.signer.annotation.GoSigner;

/**
 * @title SignerController.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:20:39
 */
@RestController
@RequestMapping("/signer")
@GoLogger(storage = LogStorageSimpleProvider.class)
public class SignerController extends BaseController {

    /**
     * 签名效验
     *
     * @param p
     * @return
     */
    @GoSigner
    @RequestMapping("/sign")
    public Result sign(String p1, String p2) {
        return R.succ(Lists.newArrayList(p1, p2));
    }

    /**
     * 忽略签名
     *
     * @param p
     * @return
     */
    @GoSigner(ignore = true)
    @RequestMapping("/sign/ignore")
    public Result ignore(String p1, String p2) {
        return R.succ(Lists.newArrayList(p1, p2));
    }
}
