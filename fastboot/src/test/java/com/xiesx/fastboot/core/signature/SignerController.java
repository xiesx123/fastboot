package com.xiesx.fastboot.core.signature;

import com.google.common.collect.Lists;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.signature.annotation.GoSigner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("signer")
public class SignerController {

  /** 签名效验 */
  @GoSigner
  @GetMapping("sign")
  public Result sign(String p1, String p2) {
    return R.succ(Lists.newArrayList(p1, p2));
  }

  /** 忽略签名 */
  @GoSigner(ignore = true)
  @GetMapping("sign/ignore")
  public Result ignore(String p1, String p2) {
    return R.succ(Lists.newArrayList(p1, p2));
  }
}
