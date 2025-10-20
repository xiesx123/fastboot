package com.xiesx.fastboot.core.signature.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = SignerProperties.PREFIX)
public class SignerProperties {

  public static final String PREFIX = "fastboot.signer";

  private String header = "sign";

  private String secret = "fastboot!@#";
}
