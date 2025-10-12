package com.xiesx.fastboot.app.log;

import cn.hutool.core.util.ObjectUtil;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.app.base.BaseVo;
import com.xiesx.fastboot.base.page.PR;
import com.xiesx.fastboot.base.page.PResult;
import com.xiesx.fastboot.base.page.PageVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("log")
public class LogRecordController extends BaseController {

    /** 分页 */
    @GetMapping("page")
    public PResult page(BaseVo base, PageVo page) {
        // 条件
        Predicate predicate = ql.id.isNotNull();
        if (ObjectUtil.isNotEmpty(base.getKeyword())) {
            Predicate p1 = ql.id.like("%" + base.getKeyword() + "%");
            Predicate p2 = ql.method.likeIgnoreCase("%" + base.getKeyword() + "%");
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
