package com.xiesx.fastboot.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RunExceptionTest {

  @Test
  void testRunExceptionWithMessage() {
    RunException exception = new RunException("Test message");
    assertEquals(RunExc.RUNTIME.getCode(), exception.getStatus());
    assertEquals("Test message", exception.getMessage());
  }

  @Test
  void testRunExceptionWithCodeAndMessage() {
    RunException exception = new RunException(400, "Bad Request");
    assertEquals(400, exception.getStatus());
    assertEquals("Bad Request", exception.getMessage());
  }

  @Test
  void testRunExceptionWithRunExc() {
    RunExc runExc = RunExc.RUNTIME;
    RunException exception = new RunException(runExc);
    assertEquals(runExc.getCode(), exception.getStatus());
    assertEquals(runExc.getMsg(), exception.getMessage());
  }

  @Test
  void testRunExceptionWithRunExcAndMessage() {
    RunExc runExc = RunExc.RUNTIME;
    RunException exception = new RunException(runExc, "Additional message");
    assertEquals(runExc.getCode(), exception.getStatus());
    assertEquals(runExc.getMsg() + ": Additional message", exception.getMessage());
  }

  @Test
  void testRunExceptionWithRunExcAndFormattedMessage() {
    RunExc runExc = RunExc.RUNTIME;
    RunException exception = new RunException(runExc, "Formatted {} message", "test");
    assertEquals(runExc.getCode(), exception.getStatus());
    assertEquals("Formatted test message", exception.getMessage());
  }

  @Test
  void testRunExceptionWithThrowable() {
    Throwable cause = new RuntimeException("Cause message");
    RunException exception = new RunException(cause);
    assertEquals(RunExc.RUNTIME.getCode(), exception.getStatus());
    assertEquals(cause, exception.getCause());
  }

  @Test
  void testRunExceptionWithThrowableAndRunExc() {
    Throwable cause = new RuntimeException("Cause message");
    RunExc runExc = RunExc.RUNTIME;
    RunException exception = new RunException(cause, runExc);
    assertEquals(runExc.getCode(), exception.getStatus());
    assertEquals(cause, exception.getCause());
  }

  @Test
  void testRunExceptionWithThrowableRunExcAndMessage() {
    Throwable cause = new RuntimeException("Cause message");
    RunExc runExc = RunExc.RUNTIME;
    RunException exception = new RunException(cause, runExc, "Custom message");
    assertEquals(runExc.getCode(), exception.getStatus());
    assertEquals("Custom message", exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
  void testRunExceptionWithThrowableRunExcAndFormattedMessage() {
    Throwable cause = new RuntimeException("Cause message");
    RunExc runExc = RunExc.RUNTIME;
    RunException exception = new RunException(cause, runExc, "Formatted {} message", "test");
    assertEquals(runExc.getCode(), exception.getStatus());
    assertEquals("Formatted test message", exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}
