package com.xiesx.fastboot.core.exception;

import java.sql.SQLSyntaxErrorException;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;

import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.app.base.BaseController;
import com.xiesx.fastboot.app.base.BaseVo;

import lombok.extern.log4j.Log4j2;

/**
 * @title ExceptionController.java
 * @description
 * @author xiesx
 * @date 2021-04-05 17:27:35
 */
@Log4j2
@Validated
@RestController
@RequestMapping("/exception")
public class ExceptionController extends BaseController {

    /**
     * 运行
     * 
     * @return
     */
    @RequestMapping(value = "runtime")
    public void runtime() {
        log.debug("空指针,抛{}异常", NullPointerException.class);
        throw new NullPointerException("手动抛空指针异常");
    }

    /**
     * 请求
     * 
     * @return
     */
    @RequestMapping(value = "request", method = RequestMethod.POST)
    public void request() {
        log.debug("非post请求,抛{}异常", HttpRequestMethodNotSupportedException.class);
    }

    /**
     * 效验(hibernate)
     * 
     * @return
     */
    @RequestMapping(value = "validator")
    public void validator(@NotBlank String p) {
        log.debug("参数空,抛{}异常", ConstraintViolationException.class);
    }

    /**
     * 效验(spring)
     * 
     * @return
     */
    @RequestMapping(value = "validator2")
    public void validator(@Validated BaseVo base) {
        log.debug("参数空,抛{}异常", BindException.class);
    }

    /**
     * 数据库
     * 
     * @return
     */
    @RequestMapping(value = "database")
    public void database() {
        log.debug("无此表,抛{}异常", SQLSyntaxErrorException.class);
        mJdbcTemplatePlus.mNamedParameterJdbcTemplate.queryForMap("select * from xx_test", Maps.newHashMap());
    }

    /**
     * 自定义
     * 
     * @return
     */
    @RequestMapping(value = "custom")
    public void custom() {
        log.debug("自定义,抛{}异常", RunException.class);
        throw new RunException(RunExc.SIGN);
    }
}
