package com.xiesx.fastboot;

import org.quartz.Scheduler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.xiesx.fastboot.support.scheduler.ScheduleHelper;
import com.xiesx.fastboot.support.scheduler.decorator.ISchedule;
import com.xiesx.fastboot.support.scheduler.decorator.SchedulerDecorator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class SpringContext implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

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
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
        if (ObjectUtil.isNotNull(applicationContext)) {
            log.info("Spring ApplicationContext completed.");
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            servername = FileUtil.getName(System.getProperty("user.dir"));
            serverpath = System.getProperty("user.dir");
            log.info("Startup Server name: " + servername + ", path: " + serverpath);

            if (SpringHelper.hasBean(Scheduler.class).isPresent()) {
                ISchedule job = new SchedulerDecorator();
                job.init();
                log.info("Startup Scheduler {} Job Completed.", ScheduleHelper.queryAllJob().size());
            }
        }
    }
}
