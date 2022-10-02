package com.xiesx.fastboot.core.logger;

import org.jboss.logging.MDC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.app.base.BaseVo;
import com.xiesx.fastboot.base.page.PageVo;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;
import com.xiesx.fastboot.support.async.Async;
import com.xiesx.fastboot.support.request.RequestTest;
import com.xiesx.fastboot.support.request.Requests;
import com.xiesx.fastboot.support.retry.RetryResponse;

import lombok.extern.log4j.Log4j2;
import net.dongliu.requests.RawResponse;

/**
 * @title LoggerController.java
 * @description
 * @author xiesx
 * @date 2021-04-05 17:27:35
 */
@Log4j2
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


    /**
     * MDC
     *
     * @return
     */
    @GoLogger(format = false, storage = LogStorageSimpleProvider.class)
    @RequestMapping("/mdc")
    public Result mdc() {
        Async.submit(() -> {
            RawResponse res = Requests.get(RequestTest.URL).send();
            RetryResponse result = res.readToJson(RetryResponse.class);
            log.info("{}", result.getTrace());
        });
        return R.succ(MDC.getMap());
    }
}
