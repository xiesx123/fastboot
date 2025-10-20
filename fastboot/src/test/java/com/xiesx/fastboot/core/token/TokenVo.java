package com.xiesx.fastboot.core.token;

import com.xiesx.fastboot.core.token.header.RequestHeaderParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TokenVo extends RequestHeaderParams {

  public String token;

  public String h1;
}
