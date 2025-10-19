package com.xiesx.fastboot.core.advice;

import com.xiesx.fastboot.base.config.Configed;
import com.xiesx.fastboot.base.page.PR;
import com.xiesx.fastboot.base.page.PResult;
import com.xiesx.fastboot.base.page.PageVo;
import com.xiesx.fastboot.base.result.R;
import com.xiesx.fastboot.base.result.Result;
import com.xiesx.fastboot.core.advice.annotation.RestBodyIgnore;
import com.xiesx.fastboot.core.logger.annotation.GoLogger;
import com.xiesx.fastboot.core.logger.storage.LogStorageProviderSimple;
import com.xiesx.fastboot.test.base.BaseController;
import com.xiesx.fastboot.test.mock.MockData;
import com.xiesx.fastboot.test.mock.MockUser;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("body")
@GoLogger(storage = LogStorageProviderSimple.class)
public class BodyController extends BaseController {

    @GetMapping("result")
    public Result result() {
        return R.succ(MockData.map());
    }

    @GetMapping("page")
    public PResult page(PageVo vo) {
        return PR.create(mLogRecordRepository.findAll(PageRequest.of(vo.getPage(), vo.getSize())));
    }

    @GetMapping("map")
    public Map<String, Object> map() {
        return MockData.map();
    }

    @GetMapping("list")
    public List<String> list() {
        return MockData.list();
    }

    @GetMapping("string")
    public String string() {
        return Configed.FASTBOOT;
    }

    @GetMapping("object")
    public MockUser object() {
        return MockData.user();
    }

    @GetMapping("ignore")
    public MockUser ignoreYml() {
        return MockData.user();
    }

    @RestBodyIgnore
    @GetMapping("ignore/annotation")
    public MockUser ignoreAnnotation() {
        return MockData.user();
    }
}
