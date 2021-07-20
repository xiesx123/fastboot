package com.xiesx.fastboot;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import com.xiesx.fastboot.support.scheduler.ScheduleHelper;
import com.xiesx.fastboot.support.scheduler.decorator.ISchedule;
import com.xiesx.fastboot.support.scheduler.decorator.SchedulerDecorator;
import com.xiesx.fastboot.utils.RuntimeUtils;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
public class SpringStartup implements InitializingBean {

    public static String classUrl;

    public static String servername;

    public static String serverpath;

    public SpringStartup() {
        log.info("SpringStartup constructor");
    }

    @PostConstruct
    public void postConstruct() {
        log.info("SpringStartup postConstruct");
    }

    public void initMethod() {
        log.info("SpringStartup initMethod");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("SpringStartup afterPropertiesSet");
    }

    public static void init() {
        classUrl = RuntimeUtils.getRootPath().toLowerCase();
        if (classUrl.contains("target")) {// 本地
            int index = classUrl.indexOf("/target");
            if (index > 0) {
                String path = classUrl.substring(0, index);
                index = path.lastIndexOf("/");
                servername = path.substring(index + 1);
                serverpath = classUrl.split(servername)[0] + servername;
            }
        } else if (classUrl.contains("web-inf")) {// tomcat启动
            int index = classUrl.indexOf("/web-inf");
            if (index > 0) {
                String path = classUrl.substring(0, index);
                index = path.lastIndexOf("/");
                servername = path.substring(index + 1);
                serverpath = classUrl.split(servername)[0] + servername;
            }
        } else if (classUrl.contains("webapps")) {// jar启动
            int index = classUrl.indexOf("/webapps");
            if (index > 0) {
                String path = classUrl.substring(0, index);
                index = path.lastIndexOf("/");
                servername = path.substring(index + 1);
                serverpath = classUrl.split(servername)[0] + servername;
            }
        } else {
            servername = "unknown";
            serverpath = classUrl;
        }
        log.info("Startup Server name: " + servername + ", path: " + serverpath);
    }

    public static void scheduler() {
        try {
            // 默认
            ISchedule job = new SchedulerDecorator();
            // 开始初始化....
            job.init();
            log.info("Startup Scheduler {} Job Completed.", ScheduleHelper.queryAllJob().size());
        } catch (Exception e) {
            log.error("Startup Scheduler {}", ExceptionUtil.getMessage(e));
        }
    }
}
