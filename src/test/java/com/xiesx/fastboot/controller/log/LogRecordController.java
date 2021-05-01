package com.xiesx.fastboot.controller.log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.app.base.BaseVo;
import com.xiesx.fastboot.base.page.PR;
import com.xiesx.fastboot.base.page.PResult;
import com.xiesx.fastboot.base.page.PageVo;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;

/**
 * @title LoggerController.java
 * @description
 * @author xiesx
 * @date 2021-04-05 17:27:35
 */
@RestController
@RequestMapping("/log")
public class LogRecordController extends BaseController {

    /**
     * 分页
     *
     * @param base
     * @param page
     * @return
     */
    @GoLogger
    @RequestMapping(value = "page")
    public PResult page(BaseVo base, PageVo page) {
        // 分页
        Pageable pageable = PageRequest.of(page.getPage(), page.getLimit());
        // 查询
        Page<LogRecord> data = mLogRecordRepository.findAll(pageable);
        // 构造
        return PR.create(data);
    }
}
