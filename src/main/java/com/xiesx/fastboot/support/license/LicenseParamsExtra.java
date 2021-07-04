package com.xiesx.fastboot.support.license;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @title LicenseParamsExtra.java
 * @description 自定义需要校验的License参数，可以增加一些额外需要校验的参数，比如项目信息，ip地址信息等等，待完善
 * @author xiesx
 * @date 2020-7-21 22:34:10
 */
@Data
@Accessors(chain = true)
public class LicenseParamsExtra implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 可被允许的MAC地址
     */
    private List<String> macAddress;
}
