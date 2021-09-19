package com.xiesx.fastboot.core.exception;

import cn.hutool.core.exceptions.StatefulException;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @title RunException.java
 * @description 自定义异常
 * @author xiesx
 * @date 2020-7-21 22:31:17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RunException extends StatefulException {

    private static final long serialVersionUID = 1L;

    /**
     * throw new RunException("出错啦！");
     *
     * @param msg
     */
    public RunException(String msg) {
        super(RunExc.RUNTIME.getCode(), msg);
    }

    /**
     * throw new RunException(100,"出错啦！");
     *
     * @param msg
     */
    public RunException(int code, String msg) {
        super(code, msg);
    }

    /**
     * throw new RunException(RunExc.RUN);
     *
     * @param rxc
     */
    public RunException(RunExc rxc) {
        super(rxc.getCode(), rxc.getMsg());
    }

    /**
     * throw new RunException(RunExc.RUN,"处理失败");
     *
     * @param rxc
     * @param msg
     */
    public RunException(RunExc rxc, String msg) {
        super(rxc.getCode(), rxc.getMsg() + ":" + msg);
    }

    /**
     * throw new RunException(RunExc.RUN,"{}处理失败","xxx");
     *
     * @param rxc
     * @param format
     * @param msg
     */
    public RunException(RunExc rxc, String format, Object... msg) {
        super(rxc.getCode(), StrUtil.format(format, msg));
    }

    /**
     * throw new RunException(e);
     *
     * @param e
     */
    public RunException(Throwable e) {
        super(RunExc.RUNTIME.getCode(), e);
    }

    /**
     * throw new RunException(e,RunExc.RUN);
     *
     * @param rxc
     * @param msg
     */
    public RunException(Throwable e, RunExc rxc) {
        super(rxc.getCode(), e);
    }

    /**
     * throw new RunException(e,RunExc.RUN,"处理失败");
     *
     * @param rxc
     * @param format
     * @param msg
     */
    public RunException(Throwable e, RunExc rxc, String msg) {
        super(rxc.getCode(), msg, e);
    }

    /**
     * throw new RunException(e,RunExc.RUN,"{}处理失败","xxx");
     *
     * @param rxc
     * @param format
     * @param msg
     */
    public RunException(Throwable e, RunExc rxc, String format, Object... msg) {
        super(rxc.getCode(), StrUtil.format(format, msg), e);
    }
}
