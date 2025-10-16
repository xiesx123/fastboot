package com.xiesx.fastboot.core.logger;

import com.xiesx.fastboot.base.page.PageVo;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;
import com.xiesx.fastboot.support.async.Async;
import com.xiesx.fastboot.support.async.AsyncTest.MyFutureCallable;
import com.xiesx.fastboot.support.async.AsyncTest.MyRunnable;
import com.xiesx.fastboot.test.base.BaseVo;
import com.yomahub.tlog.context.TLogContext;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logger")
public class LoggerController {

    @GoLogger
    @GetMapping("print")
    public Result print(BaseVo base, PageVo page) {
        return R.succ();
    }

    @GoLogger(print = false)
    @GetMapping("noprint")
    public Result noprint(BaseVo base, PageVo page) {
        return R.succ();
    }

    @GoLogger(format = true)
    @GetMapping("format")
    public Result format(BaseVo base, PageVo page) {
        return R.succ();
    }

    @GoLogger(format = true, storage = LogStorageSimpleProvider.class)
    @GetMapping("storage")
    public Result storage(BaseVo base, PageVo page) {
        return R.succ();
    }

    @GoLogger(format = false, storage = LogStorageSimpleProvider.class)
    @GetMapping("mdc")
    public Result mdc() {
        Async.submit(new MyRunnable("1"));
        Async.submit(new MyFutureCallable("3"));
        return R.succ(TLogContext.getTraceId());
    }
}
