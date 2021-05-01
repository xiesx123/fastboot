package com.xiesx.fastboot.controller.user;

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
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;

import cn.hutool.core.util.ObjectUtil;

/**
 * @title BodyController.java
 * @description
 * @author xiesx
 * @date 2021-04-03 15:49:29
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

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
        // 对象
        QUser qSimple = QUser.user;
        // 条件
        Predicate predicate = qSimple.id.isNotNull();
        if (ObjectUtil.isNotEmpty(base.getKeyword())) {
            Predicate p1 = qSimple.id.like("%" + base.getKeyword() + "%");
            Predicate p2 = qSimple.username.likeIgnoreCase("%" + base.getKeyword() + "%");
            predicate = ExpressionUtils.and(predicate, ExpressionUtils.anyOf(p1, p2));
        }
        // 排序
        Sort sort = Sort.by(Direction.ASC, User.FIELDS.createDate);
        // 分页
        Pageable pageable = PageRequest.of(page.getPage(), page.getLimit(), sort);
        // 查询
        Page<User> data = mSimpleRepository.findAll(predicate, pageable);
        // 构造
        return PR.create(data);
    }

    /**
     * 修改类型
     *
     * @param base
     * @param req
     * @return
     */
    @RequestMapping(value = "updateType")
    public Result updateType(BaseVo base, User req) {
        Integer row = 0;
        if (ObjectUtil.isNotEmpty(req.getType())) {
            row = mSimpleService.updateType(req.getType(), base.getId());
        }
        return (row >= 1) ? R.succ("修改成功") : R.fail("修改失败");
    }
}
