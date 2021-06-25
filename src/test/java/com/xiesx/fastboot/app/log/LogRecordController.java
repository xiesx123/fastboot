package com.xiesx.fastboot.app.log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.app.base.BaseVo;
import com.xiesx.fastboot.base.page.PR;
import com.xiesx.fastboot.base.page.PResult;
import com.xiesx.fastboot.base.page.PageVo;

import cn.hutool.core.util.ObjectUtil;

/**
 * @title LogRecordController.java
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
    @RequestMapping(value = "page")
    public PResult page(BaseVo base, PageVo page) {
        // 对象
        QLogRecord qLogRecord = QLogRecord.logRecord;
        // 条件
        Predicate predicate = qLogRecord.id.isNotNull();
        if (ObjectUtil.isNotEmpty(base.getKeyword())) {
            Predicate p1 = qLogRecord.id.like("%" + base.getKeyword() + "%");
            Predicate p2 = qLogRecord.method.likeIgnoreCase("%" + base.getKeyword() + "%");
            predicate = ExpressionUtils.and(predicate, ExpressionUtils.anyOf(p1, p2));
        }
        // 排序
        Sort sort = Sort.by(Direction.ASC, LogRecord.FIELDS.createDate);
        // 分页
        Pageable pageable = PageRequest.of(page.getPage(), page.getLimit(), sort);
        // 查询
        Page<LogRecord> data = mLogRecordRepository.findAll(predicate, pageable);
        // 构造
        return PR.create(data);
    }
}
