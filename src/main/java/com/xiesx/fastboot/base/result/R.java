package com.xiesx.fastboot.base.result;

import lombok.NonNull;

/**
 * @title R.java
 * @description
 * @author xiesx
 * @date 2021-04-06 09:58:49
 */
public class R {

    public static Integer CODE_SUCCESS = 0;

    public static Integer CODE_FAIL = -1;

    public static Integer CODE_ERROR = -2;

    public static Integer CODE_RETRY = -3;

    public static String MSG_SUCCESS = "操作成功";

    public static String MSG_FAIL = "操作失败";

    public static String MSG_ERROR = "操作异常";

    public static String MSG_RETRY = "重试失败";

    /**
     * 初始化
     *
     * @param code
     * @param msg
     */
    public static void initSuccess(Integer code, String msg) {
        CODE_SUCCESS = code;
        MSG_SUCCESS = msg;
    }

    public static void initFail(Integer code, String msg) {
        CODE_FAIL = code;
        MSG_FAIL = msg;
    }

    public static void initError(Integer code, String msg) {
        CODE_ERROR = code;
        MSG_ERROR = msg;
    }

    public static void initRetry(Integer code, String msg) {
        CODE_RETRY = code;
        MSG_RETRY = msg;
    }

    /**
     * 成功
     */
    public static Result succ() {
        return Result.builder().code(CODE_SUCCESS).msg(MSG_SUCCESS).build();
    }

    public static Result succ(@NonNull Object data) {
        return Result.builder().code(CODE_SUCCESS).msg(MSG_SUCCESS).data(data).build();
    }


    public static Result succ(@NonNull String msg) {
        return Result.builder().code(CODE_SUCCESS).msg(msg).build();
    }

    public static Result succ(@NonNull String msg, @NonNull Object data) {
        return Result.builder().code(CODE_SUCCESS).msg(msg).data(data).build();
    }

    public static Result succ(@NonNull Integer code, @NonNull String msg) {
        return Result.builder().code(code).msg(msg).build();
    }

    public static Result succ(@NonNull Integer code, @NonNull String msg, @NonNull Object data) {
        return Result.builder().code(code).msg(msg).data(data).build();
    }

    /**
     * 失败
     */
    public static Result fail() {
        return Result.builder().code(CODE_FAIL).msg(MSG_FAIL).build();
    }

    public static Result fail(@NonNull Object data) {
        return Result.builder().code(CODE_FAIL).msg(MSG_FAIL).data(data).build();
    }

    public static Result fail(@NonNull String msg) {
        return Result.builder().code(CODE_FAIL).msg(msg).build();
    }

    public static Result fail(@NonNull String msg, Object data) {
        return Result.builder().code(CODE_FAIL).msg(msg).data(data).build();
    }

    public static Result fail(@NonNull Integer code, @NonNull String msg) {
        return Result.builder().code(code).msg(msg).build();
    }

    public static Result fail(@NonNull Integer code, @NonNull String msg, Object data) {
        return Result.builder().code(code).msg(msg).data(data).build();
    }

    /**
     * 异常
     */
    public static Result error() {
        return Result.builder().code(CODE_ERROR).msg(MSG_FAIL).build();
    }

    public static Result error(@NonNull Object data) {
        return Result.builder().code(CODE_ERROR).msg(MSG_FAIL).data(data).build();
    }

    public static Result error(@NonNull String msg) {
        return Result.builder().code(CODE_ERROR).msg(msg).build();
    }

    public static Result error(@NonNull String msg, Object data) {
        return Result.builder().code(CODE_ERROR).msg(msg).data(data).build();
    }

    public static Result error(@NonNull Integer code, @NonNull String msg) {
        return Result.builder().code(code).msg(msg).build();
    }

    public static Result error(@NonNull Integer code, @NonNull String msg, Object data) {
        return Result.builder().code(code).msg(msg).data(data).build();
    }

    /**
     * 重试
     */
    public static Result retry() {
        return Result.builder().code(CODE_RETRY).msg(MSG_RETRY).build();
    }

    public static Result retry(@NonNull Object data) {
        return Result.builder().code(CODE_RETRY).msg(MSG_RETRY).data(data).build();
    }

    public static Result retry(@NonNull String msg) {
        return Result.builder().code(CODE_RETRY).msg(msg).build();
    }

    public static Result retry(@NonNull String msg, Object data) {
        return Result.builder().code(CODE_RETRY).msg(msg).data(data).build();
    }

    public static Result retry(@NonNull Integer code, @NonNull String msg) {
        return Result.builder().code(code).msg(msg).build();
    }

    public static Result retry(@NonNull Integer code, @NonNull String msg, Object data) {
        return Result.builder().code(code).msg(msg).data(data).build();
    }
}
