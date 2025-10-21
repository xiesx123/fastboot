package com.xiesx.fastboot.support.validate;

import cn.hutool.core.util.ObjectUtil;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;

public class ValidatorHelper {

  private static volatile Validator validator;

  public static Validator get() {
    if (validator == null) {
      synchronized (ValidatorHelper.class) {
        if (validator == null) {
          try {
            validator = SpringHelper.getBean(Validator.class);
          } catch (Exception e) {
            throw new RunException(
                RunExc.DBASE,
                "Validation dependency is missing. Please add 'spring-boot-starter-validation' to"
                    + " your pom.xml.",
                e);
          }
        }
      }
    }
    return validator;
  }

  // =====

  public static void validate(Object object) {
    validate(object, Default.class);
  }

  public static void validate(Object object, Class<?>... groups) {
    Set<? extends ConstraintViolation<?>> violations = doValidate(object, groups);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private static Set<? extends ConstraintViolation<?>> doValidate(
      Object object, Class<?>... groups) {
    return get().validate(object, groups);
  }

  // =====

  public static List<String> extractMessage(Set<? extends ConstraintViolation<?>> violations) {
    return violations.stream().map(ConstraintViolation::getMessage).toList();
  }

  public static Map<String, String> extractPropertyAndMessage(
      Set<? extends ConstraintViolation<?>> violations) {
    return violations.stream()
        .collect(
            Collectors.toMap(
                v -> v.getPropertyPath().toString(),
                ConstraintViolation::getMessage,
                (oldMsg, newMsg) -> newMsg));
  }

  public static List<String> extractPropertyAndMessageAsList(
      Set<? extends ConstraintViolation<?>> violations) {
    return extractPropertyAndMessageAsList(violations, null);
  }

  public static List<String> extractPropertyAndMessageAsList(
      Set<? extends ConstraintViolation<?>> violations, String separator) {
    String sep = ObjectUtil.defaultIfBlank(separator, " ");
    return violations.stream().map(v -> v.getPropertyPath() + sep + v.getMessage()).toList();
  }
}
