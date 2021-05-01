package com.xiesx.fastboot.core.exception;

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
public class RunException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code;

    public RunException() {
        super();
    }

    /**
     * throw new RunException("出错啦！");
     *
     * @param message
     */
    public RunException(String message) {
        super(message);
        this.code = RunExc.RUNTIME.getCode();
    }

    /**
     * throw new RunException(e);
     *
     * @param e
     */
    public RunException(Throwable e) {
        super(e);
        this.code = RunExc.RUNTIME.getCode();
    }

    /**
     * throw new RunException(RunExc.RUN);
     *
     * @param act
     */
    public RunException(RunExc act) {
        super(act.getMsg());
        this.code = act.getCode();
    }

    /**
     * throw new RunException(RunExc.RUN,"处理失败");
     *
     * @param act
     * @param message
     */
    public RunException(RunExc act, String message) {
        super(act.getMsg() + ":" + message);
        this.code = act.getCode();
    }

    /**
     * throw new RunException(RunExc.RUN,"{}处理失败","xxx");
     *
     * @param act
     * @param format
     * @param message
     */
    public RunException(RunExc act, String format, Object... message) {
        super(act.getMsg() + ":" + String.format(format, message));
        this.code = act.getCode();
    }
}
