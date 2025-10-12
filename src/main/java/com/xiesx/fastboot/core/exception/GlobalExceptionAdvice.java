package com.xiesx.fastboot.core.exception;

import cn.hutool.core.exceptions.ExceptionUtil;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.support.validate.ValidatorHelper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import lombok.extern.log4j.Log4j2;

import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /** 运行 */
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public Result runtimeException(HttpServletRequest request, Exception e) {
        String msg = ExceptionUtil.getMessage(e);
        log.error(
                "\n"
                        + "-------------------------------------------- \n"
                        + "{} \n"
                        + "--------------------------------------------",
                msg);
        return R.build(RunExc.RUNTIME.getCode(), ExceptionUtil.getSimpleMessage(e));
    }

    /** 请求 */
    @ExceptionHandler({HttpMessageConversionException.class, ServletException.class})
    public Result requestException(HttpServletRequest request, Exception e) {
        String msg = ExceptionUtil.getMessage(e);
        log.error(
                "\n"
                        + "-------------------------------------------- \n"
                        + "{} \n"
                        + "--------------------------------------------",
                msg,
                e);
        return R.build(RunExc.REQUEST.getCode(), ExceptionUtil.getSimpleMessage(e));
    }

    /** 效验 */
    @ExceptionHandler({BindException.class, ValidationException.class})
    public Result validatorException(HttpServletRequest request, Exception e) {
        String msg = ExceptionUtil.getMessage(e);
        log.error(
                "\n"
                        + "-------------------------------------------- \n"
                        + "{} \n"
                        + "--------------------------------------------",
                msg,
                e);
        List<String> msgs = Lists.newArrayList();
        // Spring Violation 验证 --> Java Violation，这里有BindException接收
        if (e instanceof BindException) {
            BindingResult violations = ((BindException) e).getBindingResult();
            violations
                    .getFieldErrors()
                    .forEach(
                            item -> {
                                msgs.add(item.getField() + " " + item.getDefaultMessage());
                            });
        }
        // Hibernate Violation 验证 --> Java Violation，这里有ConstraintViolationException接收
        if (e instanceof ValidationException) {
            msgs.addAll(
                    ValidatorHelper.extractPropertyAndMessageAsList(
                            (ConstraintViolationException) e));
        }
        return R.build(RunExc.VALIDATOR.getCode(), RunExc.VALIDATOR.getMsg(), msgs);
    }

    /** 数据库 */
    @ExceptionHandler({DataAccessException.class})
    public Result databaseException(HttpServletRequest request, Exception e) {
        String msg = ExceptionUtil.getMessage(e);
        log.error(
                "\n"
                        + "-------------------------------------------- \n"
                        + "{} \n"
                        + "--------------------------------------------",
                msg,
                e);
        return R.build(RunExc.DBASE.getCode(), ExceptionUtil.getSimpleMessage(e));
    }

    /** 自定义 */
    @ExceptionHandler({RunException.class})
    public Result customRunException(HttpServletRequest request, RunException e) {
        String msg = ExceptionUtil.getMessage(e);
        log.error(
                "\n"
                        + "-------------------------------------------- \n"
                        + "{} \n"
                        + "--------------------------------------------",
                msg,
                e);
        return R.build(e.getStatus(), ExceptionUtil.getSimpleMessage(e));
    }
}
