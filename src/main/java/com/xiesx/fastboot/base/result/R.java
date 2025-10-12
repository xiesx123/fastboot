package com.xiesx.fastboot.base.result;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;

import com.xiesx.fastboot.base.StatusEnum;

public class R {

    public static JSONConfig jcfg =
            JSONConfig.create()
                    .setIgnoreError(true)
                    .setIgnoreNullValue(true)
                    .setTransientSupport(true);

    //
    public static Integer SUCCESS_CODE = StatusEnum.SUCCESS.getCode();
    public static String SUCCESS_MSG = StatusEnum.SUCCESS.getMsg();

    //
    public static Integer FAIL_CODE = StatusEnum.FAIL.getCode();
    public static String FAIL_MSG = StatusEnum.FAIL.getMsg();

    //
    public static Integer ERROR_CODE = StatusEnum.ERROR.getCode();
    public static String ERROR_MSG = StatusEnum.ERROR.getMsg();

    //
    public static Integer RETRY_CODE = StatusEnum.RETRY.getCode();
    public static String RETRY_MSG = StatusEnum.RETRY.getMsg();

    /** 初始化 */
    public static void initSuccess(Integer code, String msg) {
        SUCCESS_CODE = code;
        SUCCESS_MSG = msg;
    }

    public static void initFail(Integer code, String msg) {
        FAIL_CODE = code;
        FAIL_MSG = msg;
    }

    public static void initError(Integer code, String msg) {
        ERROR_CODE = code;
        ERROR_MSG = msg;
    }

    public static void initRetry(Integer code, String msg) {
        RETRY_CODE = code;
        RETRY_MSG = msg;
    }

    /** 成功 */
    public static Result succ() {
        return build(SUCCESS_CODE, SUCCESS_MSG);
    }

    public static Result succ(Object data) {
        return build(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static Result succ(String msg) {
        return build(SUCCESS_CODE, msg);
    }

    public static Result succ(String msg, Object data) {
        return build(SUCCESS_CODE, msg, data);
    }

    /** 失败 */
    public static Result fail() {
        return build(FAIL_CODE, FAIL_MSG);
    }

    public static Result fail(Object data) {
        return build(FAIL_CODE, FAIL_MSG, data);
    }

    public static Result fail(String msg) {
        return build(FAIL_CODE, msg);
    }

    public static Result fail(String msg, Object data) {
        return build(FAIL_CODE, msg, data);
    }

    /** 异常 */
    public static Result error() {
        return build(ERROR_CODE, ERROR_MSG);
    }

    public static Result error(Object data) {
        return build(ERROR_CODE, ERROR_MSG, data);
    }

    public static Result error(String msg) {
        return build(ERROR_CODE, msg);
    }

    public static Result error(String msg, Object data) {
        return build(ERROR_CODE, msg, data);
    }

    /** 重试 */
    public static Result retry() {
        return build(RETRY_CODE, RETRY_MSG);
    }

    public static Result retry(Object data) {
        return build(RETRY_CODE, RETRY_MSG, data);
    }

    public static Result retry(String msg) {
        return build(RETRY_CODE, msg);
    }

    public static Result retry(String msg, Object data) {
        return build(RETRY_CODE, msg, data);
    }

    /** 构造 */
    public static Result build(StatusEnum status) {
        return new Result().code(status.getCode()).msg(status.getMsg());
    }

    public static Result build(Integer code, String msg) {
        return new Result().code(code).msg(msg);
    }

    public static Result build(Integer code, String msg, Object data) {
        return new Result().code(code).msg(msg).data(data);
    }

    /** 解析 */
    public static JSON parse(Object obj) {
        return JSONUtil.parse(obj, jcfg);
    }

    /** 转换 */
    public static Result toBean(String json) {
        return toBean(json, Result.class);
    }

    public static <T> T toBean(String json, Class<T> beanClass) {
        return JSONUtil.toBean(json, beanClass);
    }

    public static Result toBean(Exception e) {
        return R.error(ExceptionUtil.getSimpleMessage(e));
    }

    /** 表达式 */
    public static Object eval(Object obj, String expression) {
        return JSONUtil.getByPath(parse(obj), expression);
    }

    public static <T> T eval(Object obj, String expression, T bean) {
        return JSONUtil.getByPath(parse(obj), expression, bean);
    }

    /** 格式化 */
    public static String toJsonStr(Object obj) {
        return JSONUtil.toJsonStr(obj, jcfg);
    }

    public static String toJsonPrettyStr(Object obj) {
        return JSONUtil.toJsonPrettyStr(obj);
    }
}
