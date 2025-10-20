package com.xiesx.fastboot.test.base;

import com.google.common.base.Objects;
import com.xiesx.fastboot.base.IStatus;
import com.xiesx.fastboot.base.result.R;
import java.io.Serializable;
import lombok.Data;

@Data
public class BaseResult<T> implements IStatus, Serializable {

  private static final long serialVersionUID = 1L;

  public Integer code;

  public String msg;

  public T data;

  @Override
  public boolean isSuccess() {
    return Objects.equal(code, R.SUCCESS_CODE);
  }
}
