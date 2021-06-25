package com.xiesx.fastboot.support.scheduler.decorator;

import java.util.Map;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.support.scheduler.ScheduleHelper;
import com.xiesx.fastboot.support.scheduler.job.TimeJob;

/**
 * @title DefaultDecorator.java
 * @description
 * @author xiesx
 * @date 2020-7-21 22:42:58
 */
public class SchedulerDecorator extends BaseDecorator implements ISchedule {

    public SchedulerDecorator() {
        super();
    }

    public SchedulerDecorator(ISchedule decoratedJob) {
        super(decoratedJob);
    }

    @Override
    public void init() {
        if (isStart()) {
            Map<String, String> map = Maps.newConcurrentMap();
            map.put("key", "time ");
            ScheduleHelper.addJob(TimeJob.simple_job_name, TimeJob.class, "0/10 * * * * ?", map);
        }
    }

    @Override
    public boolean isStart() {
        return false;
    }
}
