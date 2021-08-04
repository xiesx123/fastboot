package com.xiesx.fastboot.core.exception;

import cn.hutool.core.text.CharSequenceUtil;
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
     * @param msg
     */
    public RunException(String msg) {
        super(msg);
        this.code = RunExc.RUNTIME.getCode();
    }

    /**
     * throw new RunException(-1,"出错啦！");
     *
     * @param msg
     */
    public RunException(Integer code, String msg) {
        super(msg);
        this.code = code;
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
     * @param rxc
     */
    public RunException(RunExc rxc) {
        super(rxc.getMsg());
        this.code = rxc.getCode();
    }

    /**
     * throw new RunException(RunExc.RUN,"处理失败");
     *
     * @param rxc
     * @param msg
     */
    public RunException(RunExc rxc, String msg) {
        super(rxc.getMsg() + ":" + msg);
        this.code = rxc.getCode();
    }

    /**
     * throw new RunException(RunExc.RUN,"{}处理失败","xxx");
     *
     * @param rxc
     * @param format
     * @param msg
     */
    public RunException(RunExc rxc, String format, Object... msg) {
        super(rxc.getMsg() + ":" + CharSequenceUtil.format(format, msg));
        this.code = rxc.getCode();
    }
}
