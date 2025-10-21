package com.xiesx.fastboot.support.request;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("request")
public class HttpRetryerController {

  @GetMapping("request")
  public Result request() {
    // 构造请求
    HttpRequest request = HttpRequest.get(HttpRequestsTest.URL);
    // 请求重试
    HttpResponse response = HttpRequests.retry(request);
    // 解析结果
    Object lid = R.eval(response.body(), "$.data.lid");
    return R.succ(Dict.create().set("lid", lid));
  }
}
