package com.xiesx.fastboot.core.eventbus;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xiesx.fastboot.FastBootApplication;
import com.xiesx.fastboot.app.base.BaseTest;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FastBootApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class EventBusTest extends BaseTest {

    @Test
    public void request() {
        get("/event/request?p=测试");
    }

    @Test
    public void test() throws InterruptedException {
        // 发布Base消息
        EventBusHelper.submit(new SimpleEvent("测试1", true));
        // 发布Object消息
        EventBusHelper.post("测试2");
        // 模拟耗时操作，线程暂停3秒
        TimeUnit.SECONDS.sleep(3);
    }
}
