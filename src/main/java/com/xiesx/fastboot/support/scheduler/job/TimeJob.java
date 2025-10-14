package com.xiesx.fastboot.support.scheduler.job;

import com.xiesx.fastboot.support.scheduler.ScheduleHelper;

import lombok.experimental.FieldNameConstants;
import lombok.extern.log4j.Log4j2;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Log4j2
@FieldNameConstants(innerTypeName = "FIELDS")
public class TimeJob extends QuartzJobBean {

    public String job;

    public String time;

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = context.getMergedJobDataMap();
        log.info(
                "{} job {}，当前任务{}个，正在运行 {}",
                map.getString(TimeJob.FIELDS.job),
                map.getString(TimeJob.FIELDS.time),
                ScheduleHelper.queryAllJob().size(),
                ScheduleHelper.queryRunJob().size());
    }
}
