package com.xiesx.fastboot.example;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xiesx.fastboot.base.page.PR;
import com.xiesx.fastboot.base.page.PResult;
import com.xiesx.fastboot.base.page.PageVo;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Validated
@RestController
@RequestMapping("log")
public class LogRecordController {

      @Autowired JPAQueryFactory mJPAQueryFactory;

      @Autowired LogRecordRepository mLogRecordRepository;

      QLogRecord ql = QLogRecord.logRecord;
    
    /** 分页 */
    @GetMapping("page")
    public PResult page(@Validated  @NotBlank  String key, PageVo page) {
        // 条件
        Predicate predicate = ql.id.isNotNull();
        if (ObjectUtil.isNotEmpty(key)) {
            Predicate p1 = ql.id.like("%" + key + "%");
            Predicate p2 = ql.method.likeIgnoreCase("%" + key + "%");
            predicate = ExpressionUtils.or(predicate, ExpressionUtils.anyOf(p1, p2));
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
