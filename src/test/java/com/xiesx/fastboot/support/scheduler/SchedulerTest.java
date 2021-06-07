package com.xiesx.fastboot.support.scheduler;

import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.FastBootApplication;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.log4j.Log4j2;

/**
 * @title SchedulerTest.java
 * @description
 * @author xiesx
 * @date 2020-12-21 6:16:35
 */
@Log4j2
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = FastBootApplication.class)
public class SchedulerTest {

    String job = "job_test";

    @Test
    @Order(1)
    public void scheduler_simple() {

        Map<String, String> map = Maps.newConcurrentMap();
        map.put("key", "time");

        log.info("【添加A】每1秒输出一次 ");
        ScheduleHelper.addJob(job, SimpleJob.class, 1, 0, map);
        ThreadUtil.sleep(2000);

        log.info("【修改A】每2秒输出一次");
        ScheduleHelper.updateJob(job, 2, 0);
        ThreadUtil.sleep(4000);

        log.info("【移除A】");
        ScheduleHelper.deleteJob(job);
        ThreadUtil.sleep(1000);

        log.info("【添加B】每3秒输出一次");
        ScheduleHelper.addJob(job, SimpleJob.class, 3, 0, map);
        ThreadUtil.sleep(6000);

        log.info("【暂停B】");
        ScheduleHelper.pauseJob(job);
        ThreadUtil.sleep(1000);

        log.info("【恢复B】");
        ScheduleHelper.resumeJob(job);
        ThreadUtil.sleep(1000);

        log.info("【移除B】.");
        ScheduleHelper.deleteJob(job);
        ThreadUtil.sleep(1000);
    }

    @Test
    @Order(2)
    public void scheduler_cron() {

        Map<String, String> map = Maps.newConcurrentMap();
        map.put("key", "time");

        log.info("【添加A】每1秒输出一次 ");
        ScheduleHelper.addJob(job, SimpleJob.class, "0/1 * * * * ?", map);
        ThreadUtil.sleep(2000);

        log.info("【修改A】每2秒输出一次");
        ScheduleHelper.updateJob(job, "0/2 * * * * ?");
        ThreadUtil.sleep(4000);

        log.info("【移除A】");
        ScheduleHelper.deleteJob(job);
        ThreadUtil.sleep(1000);

        log.info("【添加B】每3秒输出一次");
        ScheduleHelper.addJob(job, SimpleJob.class, "*/3 * * * * ?", map);
        ThreadUtil.sleep(6000);

        log.info("【暂停B】");
        ScheduleHelper.pauseJob(job);
        ThreadUtil.sleep(1000);

        log.info("【恢复B】");
        ScheduleHelper.resumeJob(job);
        ThreadUtil.sleep(1000);

        log.info("【移除B】");
        ScheduleHelper.deleteJob(job);
        ThreadUtil.sleep(1000);
    }
}
