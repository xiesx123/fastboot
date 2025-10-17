package com.xiesx.fastboot.core.exception;

import cn.hutool.core.exceptions.StatefulException;
import cn.hutool.core.util.StrUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RunException extends StatefulException {

    private static final long serialVersionUID = 1L;

    public RunException(String msg) {
        super(RunExc.RUNTIME.getCode(), msg);
    }

    public RunException(int code, String msg) {
        super(code, msg);
    }

    public RunException(RunExc rxc) {
        super(rxc.getCode(), rxc.getMsg());
    }

    public RunException(RunExc rxc, String msg) {
        super(rxc.getCode(), rxc.getMsg() + ": " + msg);
    }

    public RunException(RunExc rxc, String format, Object... msg) {
        super(rxc.getCode(), StrUtil.format(format, msg));
    }

    public RunException(Throwable e) {
        super(RunExc.RUNTIME.getCode(), e);
    }

    public RunException(Throwable e, RunExc rxc) {
        super(rxc.getCode(), e);
    }

    public RunException(Throwable e, RunExc rxc, String msg) {
        super(rxc.getCode(), msg, e);
    }

    public RunException(Throwable e, RunExc rxc, String format, Object... msg) {
        super(rxc.getCode(), StrUtil.format(format, msg), e);
    }
}
