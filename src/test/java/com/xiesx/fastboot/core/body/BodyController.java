package com.xiesx.fastboot.core.body;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.app.mock.MockData;
import com.xiesx.fastboot.app.mock.MockUser;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.body.annotation.RestBodyIgnore;
import com.xiesx.fastboot.core.logger.LogStorageSimpleProvider;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;

/**
 * @title BodyController.java
 * @description
 * @author xiesx
 * @date 2021-04-03 15:49:29
 */
@RestController
@RequestMapping("/body")
@GoLogger(storage = LogStorageSimpleProvider.class)
public class BodyController extends BaseController {

    /**
     * FastBoot Result 类型
     *
     * @return
     */
    @RequestMapping("result")
    public Result result() {
        return R.succ(MockData.map());
    }

    /**
     * Java Map 类型
     *
     * @return
     */
    @RequestMapping("map")
    public Map<String, Object> map() {
        return MockData.map();
    }

    /**
     * Java Iterable 类型
     *
     * @return
     */
    @RequestMapping("list")
    public List<String> list() {
        return MockData.list();
    }

    /**
     * Java String 类型
     *
     * @return
     */
    @RequestMapping("string")
    public String string() {
        return "k1";
    }

    /**
     * FastJson JSON 类型
     *
     * @return
     */
    @RequestMapping("fastjson")
    public JSON json() {
        return MockData.fastjson();
    }

    /**
     * Java Object 类型
     *
     * @return
     */
    @RequestMapping("object")
    public MockUser object() {
        return MockData.user();
    }

    /**
     * @IgnoreBody忽略Advice返回
     *
     * @return
     */
    @RestBodyIgnore
    @RequestMapping("ignore")
    public MockUser ignore() {
        return MockData.user();
    }
}
