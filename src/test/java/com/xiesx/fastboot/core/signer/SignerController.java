package com.xiesx.fastboot.core.signer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.signer.annotation.GoSigner;

/**
 * @title LimiterController.java
 * @description
 * @author xiesx
 * @date 2021-04-05 17:27:35
 */
@RestController
@RequestMapping("/signer")
public class SignerController extends BaseController {

    /**
     * 签名效验
     * 
     * @param p
     * @return
     */
    @GoSigner
    @RequestMapping("/sign")
    public Result sign(String p) {
        return R.succ(Lists.newArrayList(p));
    }

    /**
     * 忽略签名
     * 
     * @param p
     * @return
     */
    @GoSigner(ignore = true)
    @RequestMapping("/limiter")
    public Result ignore(String p) {
        return R.succ(Lists.newArrayList(p));
    }
}
