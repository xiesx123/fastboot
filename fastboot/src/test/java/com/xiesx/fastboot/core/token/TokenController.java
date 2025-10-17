package com.xiesx.fastboot.core.token;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.token.annotation.GoHeader;
import com.xiesx.fastboot.core.token.annotation.GoToken;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("token")
public class TokenController {

    @GetMapping("header")
    public Result header(String name, @GoToken String uid, @GoHeader TokenVo header, TokenVo vo) {
        JSONObject headers = JwtHelper.parser(header.token).getHeaders();
        String subscribe = headers.getStr("subscribe", StrUtil.EMPTY);
        return R.succ(Lists.newArrayList(name, uid, subscribe, header, vo));
    }
}
