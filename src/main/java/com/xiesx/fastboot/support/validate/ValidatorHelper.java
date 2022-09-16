package com.xiesx.fastboot.support.validate;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;

import cn.hutool.core.lang.Assert;
import lombok.NonNull;

/**
 * @title ValidatorHelper.java
 * @description
 * @author xiesx
 * @date 2020-8-18 0:40:49
 */
public class ValidatorHelper {

    public static Validator get() {
        Validator validator = SpringHelper.getBean(Validator.class);
        Assert.notNull(validator, () -> new RunException(RunExc.DBASE, "pom need dependency spring-boot-starter-validation"));
        return validator;
    }

    /**
     * 对象效验
     *
     * @param object
     * @throws ConstraintViolationException
     */
    public static void validate(@NonNull Object object) throws ConstraintViolationException {
        Set<? extends ConstraintViolation<?>> constraintViolations = get().validate(object, Default.class);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    /**
     * 对象效验
     *
     * @param object
     * @param groups
     * @throws ConstraintViolationException
     */
    public static void validate(@NonNull Object object, Class<?>... groups) throws ConstraintViolationException {
        Set<? extends ConstraintViolation<?>> constraintViolations = get().validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    // =============

    public static List<String> extractMessage(@NonNull ConstraintViolationException e) {
        return extractMessage(e.getConstraintViolations());
    }

    public static List<String> extractMessage(@NonNull Set<? extends ConstraintViolation<?>> constraintViolations) {
        List<String> errorMessages = Lists.newArrayList();
        constraintViolations.forEach(v -> {
            errorMessages.add(v.getMessage());
        });
        return errorMessages;
    }

    // =============

    public static Map<String, String> extractPropertyAndMessage(@NonNull ConstraintViolationException e) {
        return extractPropertyAndMessage(e.getConstraintViolations());
    }

    public static Map<String, String> extractPropertyAndMessage(@NonNull Set<? extends ConstraintViolation<?>> constraintViolations) {
        Map<String, String> errorMsgs = Maps.newConcurrentMap();
        constraintViolations.forEach(v -> {
            errorMsgs.put(v.getPropertyPath().toString(), v.getMessage());
        });
        return errorMsgs;
    }

    // =============

    public static List<String> extractPropertyAndMessageAsList(@NonNull ConstraintViolationException e) {
        return extractPropertyAndMessageAsList(e.getConstraintViolations(), " ");
    }

    public static List<String> extractPropertyAndMessageAsList(@NonNull Set<? extends ConstraintViolation<?>> constraintViolations) {
        return extractPropertyAndMessageAsList(constraintViolations, " ");
    }

    public static List<String> extractPropertyAndMessageAsList(@NonNull ConstraintViolationException e, String separator) {
        return extractPropertyAndMessageAsList(e.getConstraintViolations(), separator);
    }

    public static List<String> extractPropertyAndMessageAsList(@NonNull Set<? extends ConstraintViolation<?>> constraintViolations, String separator) {
        List<String> errorMessages = Lists.newArrayList();
        constraintViolations.forEach(v -> {
            errorMessages.add(v.getPropertyPath() + separator + v.getMessage());
        });
        return errorMessages;
    }
}
