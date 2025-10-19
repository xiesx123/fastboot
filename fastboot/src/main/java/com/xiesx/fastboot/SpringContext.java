package com.xiesx.fastboot;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.system.SystemUtil;

import com.xiesx.fastboot.support.scheduler.ScheduleHelper;
import com.xiesx.fastboot.support.scheduler.decorator.ISchedule;
import com.xiesx.fastboot.support.scheduler.decorator.SchedulerDecorator;

import lombok.extern.log4j.Log4j2;

import org.quartz.Scheduler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class SpringContext
        implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private static String servername;

    private static String serverpath;

    private static ApplicationContext applicationContext;

    public static String getServerName() {
        return servername;
    }

    public static String getServerPath() {
        return serverpath;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {
        SpringContext.applicationContext = context;
        if (ObjectUtil.isNotNull(getApplicationContext())) {
            log.debug("Spring ApplicationContext completed.");
        }
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            serverpath = SystemUtil.getUserInfo().getCurrentDir();
            servername = FileUtil.getName(serverpath);
            log.info("Startup Server name: {}, path: {}", getServerName(), getServerPath());
            if (checkSchedulerClassExists() && SpringHelper.hasBean(Scheduler.class).isPresent()) {
                ISchedule job = new SchedulerDecorator();
                job.init();
                List<Map<String, Object>> jobs = ScheduleHelper.queryAllJob();
                if (!jobs.isEmpty()) {
                    log.info("Startup Scheduler {} Job Completed.", jobs.size());
                }
            }
        }
    }

    public boolean checkSchedulerClassExists() {
        return ClassLoaderUtil.isPresent("org.quartz.Scheduler");
    }
}
