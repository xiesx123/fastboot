package com.xiesx.fastboot.core.exception;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.db.jdbc.JdbcTemplatePlus;
import com.xiesx.fastboot.test.base.BaseVo;

import lombok.extern.log4j.Log4j2;

import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLSyntaxErrorException;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;

@Log4j2
@Validated
@RestController
@RequestMapping("exception")
public class ExceptionController {

    @GetMapping("runtime")
    public void runtime() {
        log.debug("空指针,抛  {}  异常", NullPointerException.class);
        throw new NullPointerException("手动抛空指针异常");
    }

    @PostMapping("request")
    public void request() {
        log.info("非post请求,抛 {} 异常", HttpRequestMethodNotSupportedException.class);
    }

    @GetMapping("hibernate/validator")
    public void hibernate_validator(@NotBlank String p) {
        log.info("参数空,抛 {} 异常", ConstraintViolationException.class);
    }

    @GetMapping("spring/validator")
    public void spring_validator(@Validated BaseVo vo) {
        log.info("参数空,抛 {} 异常", BindException.class);
    }

    @GetMapping("database")
    public void database() {
        log.info("无此表,抛 {} 异常", SQLSyntaxErrorException.class);
        JdbcTemplatePlus.queryForMap("select * from xx_test"); // 不捕获
        JdbcTemplatePlus.get().queryForMap("select * from xx_test", Maps.newHashMap()); // 全局捕获
    }

    @GetMapping("custom")
    public void custom() {
        log.info("自定义,抛 {} 异常", RunException.class);
        throw new RunException(RunExc.SIGN);
    }
}
