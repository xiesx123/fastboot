package com.xiesx.fastboot.support.request;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.support.retry.RetryResponse;

import lombok.extern.log4j.Log4j2;
import net.dongliu.requests.Parameter;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.RequestBuilder;
import net.dongliu.requests.Requests;

/**
 * @title RequestTest.java
 * @description
 * @author xiesx
 * @date 2020-8-15 14:09:29
 */
@Log4j2
public class RequestTest {

    public final static String URL = "https://api.go168.xyz/api/appConfig";

    @Test
    public void http() {
        // 构造请求
        RequestBuilder req = Requests.post(URL).params(Parameter.of("configKey", "appLaunch"));
        // 请求重试
        RawResponse response = RequestsHelper.retry(req);
        // 获取结果
        RetryResponse result = response.readToJson(RetryResponse.class);
        // 验证结果，如果结果正确则返回，错误则重试
        log.info(JSON.toJSONString(R.succ(result.getData()), true));
    }
}
