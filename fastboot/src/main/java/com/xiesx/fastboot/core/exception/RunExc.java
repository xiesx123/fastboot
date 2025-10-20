package com.xiesx.fastboot.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RunExc {
  RUNTIME(1000, "Runtime Error"), // --> GlobalExceptionAdvice --> runtimeException

  ASYNC(1010, "Execution Error"), // --> GlobalExceptionAdvice --> runtimeException

  REQUEST(2000, "Request Failed"), // --> GlobalExceptionAdvice --> requestException

  RETRY(2010, "Retry Failed"), // --> HttpRetryer

  LIMITER(2020, "Request Rate Limited"), // --> LimiterAspect

  VALIDATOR(3000, "Validation Error"), // --> GlobalExceptionAdvice --> validatorException

  DBASE(4000, "Database Error"), // --> GlobalExceptionAdvice --> jdbcException

  TOKEN(5000, "Token Error"), // --> TokenInterceptor

  SIGN(6000, "Signature Error"), // --> SignerAspect

  UNKNOWN(9999, "Unknown");

  private Integer code;

  private String msg;
}
