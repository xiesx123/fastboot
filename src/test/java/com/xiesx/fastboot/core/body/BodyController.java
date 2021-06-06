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
import com.xiesx.fastboot.core.body.annotation.IgnoreBody;
import com.xiesx.fastboot.core.logger.LogStorageMysqlProvider;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;

/**
 * @title BodyController.java
 * @description
 * @author xiesx
 * @date 2021-04-03 15:49:29
 */
@RestController
@RequestMapping("/body")
@GoLogger(storage = LogStorageMysqlProvider.class)
public class BodyController extends BaseController {

    /**
     * FastBoot Result 类型
     *
     * @return
     */
    @RequestMapping(value = "result")
    public Result result() {
        return R.succ(MockData.map());
    }

    /**
     * Java Map 类型
     * 
     * @return
     */
    @RequestMapping(value = "map")
    public Map<String, Object> map() {
        return MockData.map();
    }

    /**
     * Java Iterable 类型
     * 
     * @return
     */
    @RequestMapping(value = "list")
    public List<String> list() {
        return MockData.list();
    }

    /**
     * FastJson JSON 类型
     * 
     * @return
     */
    @RequestMapping(value = "json")
    public JSON json() {
        return MockData.json();
    }

    /**
     * Java Object 类型
     * 
     * @return
     */
    @RequestMapping(value = "object")
    public MockUser object() {
        return MockData.user();
    }

    /**
     * @IgnoreBody忽略Advice返回
     * 
     * @return
     */
    @IgnoreBody
    @RequestMapping(value = "ignore")
    public MockUser ignore() {
        return MockData.user();
    }
}
