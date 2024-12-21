package com.xiesx.fastboot.support.scheduler.decorator;

import com.xiesx.fastboot.support.scheduler.ScheduleHelper;
import com.xiesx.fastboot.support.scheduler.job.TimeJob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;

/**
 * @title SchedulerDecorator.java
 * @description
 * @author xiesx
 * @date 2022-02-27 12:42:35
 */
public class SchedulerDecorator extends AbstractDecorator implements ISchedule {

    public SchedulerDecorator() {
        super();
    }

    public SchedulerDecorator(ISchedule schedule) {
        super(schedule);
    }

    @Override
    public void init() {
        if (isRun()) {
            ScheduleHelper.addJob(TimeJob.class, "0/10 * * * * ?", Dict.create().set(TimeJob.FIELDS.time, DateUtil.now()));
        }
    }

    @Override
    public boolean isRun() {
        return false;
    }
}
