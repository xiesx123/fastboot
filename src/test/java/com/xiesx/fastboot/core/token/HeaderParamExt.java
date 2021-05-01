package com.xiesx.fastboot.core.token;

import com.xiesx.fastboot.core.token.header.HeaderParam;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HeaderParamExt extends HeaderParam {

    private String ext;
}
