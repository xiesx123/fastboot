package com.xiesx.fastboot.base.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ResultTest {

    @Test
    void testIsReTryWhenCodeIsRetryCode() {
        // Arrange
        Result result = new Result();
        result.code = R.RETRY_CODE;

        // Act
        boolean isReTry = result.isReTry();

        // Assert
        assertTrue(isReTry, "isReTry should return true when code is RETRY_CODE");
    }

    @Test
    void testIsReTryWhenCodeIsNotRetryCode() {
        // Arrange
        Result result = new Result();
        result.code = 123; // Some code other than RETRY_CODE

        // Act
        boolean isReTry = result.isReTry();

        // Assert
        assertFalse(isReTry, "isReTry should return false when code is not RETRY_CODE");
    }

    @Test
    void testIsSuccessWhenCodeIsSuccessCode() {
        // Arrange
        Result result = new Result();
        result.code = R.SUCCESS_CODE;

        // Act
        boolean isSuccess = result.isSuccess();

        // Assert
        assertTrue(isSuccess, "isSuccess should return true when code is SUCCESS_CODE");
    }

    @Test
    void testIsSuccessWhenCodeIsNotSuccessCode() {
        // Arrange
        Result result = new Result();
        result.code = 123; // Some code other than SUCCESS_CODE

        // Act
        boolean isSuccess = result.isSuccess();

        // Assert
        assertFalse(isSuccess, "isSuccess should return false when code is not SUCCESS_CODE");
    }

    @Test
    void testIsFailWhenCodeIsFailCode() {
        // Arrange
        Result result = new Result();
        result.code = R.FAIL_CODE;

        // Act
        boolean isFail = result.isFail();

        // Assert
        assertTrue(isFail, "isFail should return true when code is FAIL_CODE");
    }

    @Test
    void testIsFailWhenCodeIsNotFailCode() {
        // Arrange
        Result result = new Result();
        result.code = 123; // Some code other than FAIL_CODE

        // Act
        boolean isFail = result.isFail();

        // Assert
        assertFalse(isFail, "isFail should return false when code is not FAIL_CODE");
    }

    @Test
    void testIsErrorWhenCodeIsErrorCode() {
        // Arrange
        Result result = new Result();
        result.code = R.ERROR_CODE;

        // Act
        boolean isError = result.isError();

        // Assert
        assertTrue(isError, "isError should return true when code is ERROR_CODE");
    }

    @Test
    void testIsErrorWhenCodeIsNotErrorCode() {
        // Arrange
        Result result = new Result();
        result.code = 123; // Some code other than ERROR_CODE

        // Act
        boolean isError = result.isError();

        // Assert
        assertFalse(isError, "isError should return false when code is not ERROR_CODE");
    }

    @Test
    void testGetStatusWhenCodeIsSuccessCode() {
        // Arrange
        Result result = new Result();
        result.code = R.SUCCESS_CODE;

        // Act
        boolean status = result.getStatus();

        // Assert
        assertTrue(status, "getStatus should return true when code is SUCCESS_CODE");
    }

    @Test
    void testGetStatusWhenCodeIsNotSuccessCode() {
        // Arrange
        Result result = new Result();
        result.code = 123; // Some code other than SUCCESS_CODE

        // Act
        boolean status = result.getStatus();

        // Assert
        assertFalse(status, "getStatus should return false when code is not SUCCESS_CODE");
    }

    @Test
    void testToJsonString() {
        // Arrange
        Result result = new Result();
        result.code = R.SUCCESS_CODE;
        result.msg = "Success";
        result.data = "Test Data";

        // Act
        String jsonString = result.toJsonString();

        // Assert
        assertEquals(
                R.toJsonStr(result),
                jsonString,
                "toJsonString should return the correct JSON string");
    }

    @Test
    void testToJsonPrettyStr() {
        // Arrange
        Result result = new Result();
        result.code = R.SUCCESS_CODE;
        result.msg = "Success";
        result.data = "Test Data";

        // Act
        String jsonPrettyString = result.toJsonPrettyStr();

        // Assert
        assertEquals(
                R.toJsonPrettyStr(result),
                jsonPrettyString,
                "toJsonPrettyStr should return the correct pretty JSON string");
    }
}
