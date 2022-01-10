package com.xiesx.fastboot.base.result;

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
        return build(CODE_SUCCESS, MSG_SUCCESS);
    }

    public static Result succ(Object data) {
        return build(CODE_SUCCESS, MSG_SUCCESS, data);
    }

    public static Result succ(String msg) {
        return build(CODE_SUCCESS, msg);
    }

    public static Result succ(String msg, Object data) {
        return build(CODE_SUCCESS, msg, data);
    }

    /**
     * 失败
     */
    public static Result fail() {
        return build(CODE_FAIL, MSG_FAIL);
    }

    public static Result fail(Object data) {
        return build(CODE_FAIL, MSG_FAIL, data);
    }

    public static Result fail(String msg) {
        return build(CODE_FAIL, msg);
    }

    public static Result fail(String msg, Object data) {
        return build(CODE_FAIL, msg, data);
    }

    /**
     * 异常
     */
    public static Result error() {
        return build(CODE_ERROR, MSG_FAIL);
    }

    public static Result error(Object data) {
        return build(CODE_ERROR, MSG_FAIL, data);
    }

    public static Result error(String msg) {
        return build(CODE_ERROR, msg);
    }

    public static Result error(String msg, Object data) {
        return build(CODE_ERROR, msg, data);
    }

    /**
     * 重试
     */
    public static Result retry() {
        return build(CODE_RETRY, MSG_RETRY);
    }

    public static Result retry(Object data) {
        return build(CODE_RETRY, MSG_RETRY, data);
    }

    public static Result retry(String msg) {
        return build(CODE_RETRY, msg);
    }

    public static Result retry(String msg, Object data) {
        return build(CODE_RETRY, msg, data);
    }

    /**
     * 构造
     * 
     * @param code
     * @param msg
     * @return
     */
    public static Result build(Integer code, String msg) {
        return Result.builder().code(code).msg(msg).build();
    }

    public static Result build(Integer code, String msg, Object data) {
        return Result.builder().code(code).msg(msg).data(data).build();
    }
}
