package com.xiesx.fastboot.core.exception;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.support.validate.ValidatorHelper;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.log4j.Log4j2;

/**
 * @title GlobalExceptionAdvice.java
 * @description 全局异常处理
 * @author xiesx
 * @date 2020-7-21 22:30:43
 */
@Log4j2
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 运行
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public Result runtimeException(HttpServletRequest request, Exception e) {
        log.error("runtime exception", e);
        return R.error(RunExc.RUNTIME.getCode(), ExceptionUtil.getSimpleMessage(e));
    }

    /**
     * 请求
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler({HttpMessageConversionException.class, ServletException.class})
    public Result requestException(HttpServletRequest request, Exception e) {
        log.error("request exception", e);
        String msg = ExceptionUtil.getSimpleMessage(e);
        if (e instanceof HttpMessageConversionException) {
            msg = "当前参数解析失败"; // HttpMessageConversionException 400 - Bad Request
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            msg = "不支持当前请求方法"; // ServletException 405 - Method Not Allowed
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            msg = "不支持当前媒体类型"; // ServletException 415 - Unsupported Media Type
        }
        return R.error(RunExc.REQUEST.getCode(), msg);
    }

    /**
     * 效验
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler({BindException.class, ValidationException.class})
    public Result validatorException(HttpServletRequest request, Exception e) {
        log.error("validator exception", e);
        List<String> msgs = Lists.newArrayList();
        // Spring Violation 验证 --> Java Violation，这里有BindException接收
        if (e instanceof BindException) {
            BindingResult violations = ((BindException) e).getBindingResult();
            for (FieldError fieldError : violations.getFieldErrors()) {
                msgs.add(fieldError.getField() + " " + fieldError.getDefaultMessage());
            }
        }
        // Hibernate Violation 验证 --> Java Violation，这里有ConstraintViolationException接收
        if (e instanceof ValidationException) {
            msgs.addAll(ValidatorHelper.extractPropertyAndMessageAsList(((ConstraintViolationException) e)));
        }
        return R.error(RunExc.VALIDATOR.getCode(), RunExc.VALIDATOR.getMsg(), msgs);
    }

    /**
     * 数据库
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler({DataAccessException.class})
    public Result databaseException(HttpServletRequest request, Exception e) {
        log.error("database exception", e);
        String msg = ExceptionUtil.getSimpleMessage(e);
        if (e instanceof EmptyResultDataAccessException) {
            msg = "无数据";
        }
        return R.error(RunExc.DBASE.getCode(), msg);
    }

    /**
     * 自定义
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler({RunException.class})
    public Result customRunException(HttpServletRequest request, RunException e) {
        log.error("custom run exception", e);
        return R.error(e.getCode(), ExceptionUtil.getSimpleMessage(e));
    }
}
