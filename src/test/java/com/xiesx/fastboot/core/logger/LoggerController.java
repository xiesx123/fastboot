package com.xiesx.fastboot.core.logger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.app.base.BaseVo;
import com.xiesx.fastboot.base.page.PageVo;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;

/**
 * @title LoggerController.java
 * @description
 * @author xiesx
 * @date 2021-04-05 17:27:35
 */
@RestController
@RequestMapping("/logger")
public class LoggerController extends BaseController {

    /**
     * 不打印
     *
     * @return
     */
    @GoLogger(print = false)
    @RequestMapping("/noprint")
    public Result noprint(BaseVo base, PageVo page) {
        return R.succ();
    }

    /**
     * 默认打印 + 格式化输出
     *
     * @return
     */
    @GoLogger
    @RequestMapping("/print")
    public Result print(BaseVo base, PageVo page) {
        return R.succ();
    }


    /**
     * 格式化输出
     *
     * @return
     */
    @GoLogger(format = true)
    @RequestMapping("/format")
    public Result format(BaseVo base, PageVo page) {
        return R.succ();
    }

    /**
     * 格式化输出 + 自定义存储
     *
     * @return
     */
    @GoLogger(format = true, storage = LogStorageSimpleProvider.class)
    @RequestMapping("/storage")
    public Result storage(BaseVo base, PageVo page) {
        return R.succ();
    }
}
