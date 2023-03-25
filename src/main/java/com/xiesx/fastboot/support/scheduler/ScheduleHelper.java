package com.xiesx.fastboot.support.scheduler;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ScheduleHelper {

    private static String JOB_GROUP_NAME = "FAST_JOB_GROUP_NAME";

    public static Scheduler get() {
        Scheduler scheduler = SpringHelper.getBean(Scheduler.class);
        Assert.notNull(scheduler, () -> new RunException(RunExc.DBASE, "pom need dependency spring-boot-starter-quartz"));
        return scheduler;
    }

    // ============================

    /**
     * 增加SimpleJob
     *
     * @param cls 任务实现类
     * @param interval 时间表达式 (这是每隔多少秒为一次任务)
     * @param repeat 运行的次数 （<0:表示不限次数）
     */
    public static void addJob(Class<? extends Job> cls, int interval, int repeat) {
        // 创建
        addJob(cls.getSimpleName(), cls, interval, repeat, null);
    }

    /**
     * 增加SimpleJob
     *
     * @param job 任务名称
     * @param cls 任务实现类
     * @param interval 时间表达式 (这是每隔多少秒为一次任务)
     * @param repeat 运行的次数 （<0:表示不限次数）
     */
    public static void addJob(String job, Class<? extends Job> cls, int interval, int repeat) {
        // 创建
        addJob(job, cls, interval, repeat, null);
    }

    /**
     * 增加SimpleJob
     *
     * @param job 任务名称
     * @param cls 任务实现类
     * @param interval 时间表达式 (这是每隔多少秒为一次任务)
     * @param repeat 运行的次数 （<0:表示不限次数）
     * @param data 参数
     */
    public static void addJob(String job, Class<? extends Job> cls, int interval, int repeat, Map<? extends String, ? extends Object> data) {
        // 构建SimpleScheduleBuilder规则
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()//
                // 几秒钟重复执行
                .withIntervalInSeconds(interval);
        if (repeat > 0) {
            // 重复次数
            simpleScheduleBuilder.withRepeatCount(repeat);
        }
        // 一直执行
        simpleScheduleBuilder.repeatForever();
        // 创建
        createJob(job, cls, simpleScheduleBuilder, data);
    }

    /**
     * 增加SimpleJob
     *
     * @param job 任务名称
     * @param group 任务组名
     * @param cls 任务实现类
     * @param interval 时间表达式 (这是每隔多少秒为一次任务)
     * @param repeat 运行的次数 （<0:表示不限次数）
     * @param data 参数
     */
    public static void addJob(String job, String group, Class<? extends Job> cls, int interval, int repeat, Map<? extends String, ? extends Object> data) {
        // 构建SimpleScheduleBuilder规则
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                // 几秒钟重复执行
                .withIntervalInSeconds(interval);
        if (repeat > 0) {
            // 重复次数
            simpleScheduleBuilder.withRepeatCount(repeat);
        }
        // 一直执行
        simpleScheduleBuilder.repeatForever();
        // 创建
        createJob(job, group, cls, simpleScheduleBuilder, data);
    }

    /**
     * 增加CronJob
     *
     * @param cls 任务实现类
     * @param cron 时间表达式 （如：0/5 * * * * ? ）
     */
    public static void addJob(Class<? extends Job> cls, String cron) {
        addJob(cls.getSimpleName(), cls, cron, null);
    }

    /**
     * 增加CronJob
     *
     * @param job 任务名称
     * @param cls 任务实现类
     * @param cron 时间表达式 （如：0/5 * * * * ? ）
     */
    public static void addJob(String job, Class<? extends Job> cls, String cron) {
        addJob(job, cls, cron, null);
    }

    /**
     * 增加CronJob
     *
     * @param job 任务名称
     * @param cls 任务实现类
     * @param cron 时间表达式 （如：0/5 * * * * ? ）
     * @param data 参数
     */
    public static void addJob(String job, Class<? extends Job> cls, String cron, Map<? extends String, ? extends Object> data) {
        // 构建CronScheduleBuilder规则
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        // 创建
        createJob(job, cls, cronScheduleBuilder, data);
    }

    /**
     * 增加CronJob
     *
     * @param job 任务名称
     * @param group 任务组名
     * @param cls 任务实现类
     * @param cron 时间表达式 （如：0/5 * * * * ? ）
     * @param data 参数
     */
    public static void addJob(String job, String group, Class<? extends Job> cls, String cron, Map<? extends String, ? extends Object> data) {
        // 构建CronScheduleBuilder规则
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        // 创建
        createJob(job, group, cls, cronScheduleBuilder, data);
    }

    // ============================

    /**
     * 创建
     *
     * @param cls 任务实现类
     * @param ScheduleBuilder 时间表达式 (这是每隔多少秒为一次任务)
     * @param data 参数
     */
    public static void createJob(String job, Class<? extends Job> cls, ScheduleBuilder<? extends Trigger> ScheduleBuilder, Map<? extends String, ? extends Object> data) {
        createJob(job, JOB_GROUP_NAME, cls, ScheduleBuilder, data);
    }

    /**
     * 创建
     *
     * @param job 任务名称
     * @param group 任务组名
     * @param cls 任务实现类
     * @param interval 时间表达式 (这是每隔多少秒为一次任务)
     * @param repeat 运行的次数 （<0:表示不限次数）
     * @param data 参数
     */
    public static void createJob(String job, String group, Class<? extends Job> cls, ScheduleBuilder<? extends Trigger> ScheduleBuilder, Map<? extends String, ? extends Object> data) {
        try {
            // 构建实例
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(job, group).build();
            // 设置参数
            if (MapUtil.isNotEmpty(data)) {
                jobDetail.getJobDataMap().putAll(data);
            }
            // 构建触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(job, group);
            triggerBuilder.withSchedule(ScheduleBuilder);
            triggerBuilder.startNow();
            get().scheduleJob(jobDetail, triggerBuilder.build());
        } catch (SchedulerException e) {
            log.error("create job error {}", e.getMessage());
        }
    }

    // ============================

    /**
     * 修改
     *
     * @param cron
     */
    public static void updateJob(String job, int interval, int repeat) {
        updateJob(job, JOB_GROUP_NAME, interval, repeat);
    }

    /**
     * 修改
     *
     * @param cron
     */
    public static void updateJob(String job, String cron) {
        updateJob(job, JOB_GROUP_NAME, cron);
    }

    /**
     * 修改
     *
     * @param job
     * @param group
     * @param cron
     */
    public static void updateJob(String job, String group, int interval, int repeat) {
        Scheduler scheduler = get();
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(job, group);
            SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
            //
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule() // 几秒钟重复执行
                    .withIntervalInSeconds(interval);
            if (repeat > 0) {
                // 重复次数
                simpleScheduleBuilder.withRepeatCount(repeat);
            }
            // 一直执行
            simpleScheduleBuilder.repeatForever();
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(simpleScheduleBuilder).build();
            // 重启触发器
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("update job error {}", e.getMessage());
        }
    }

    /**
     * 修改
     *
     * @param job
     * @param group
     * @param cron
     */
    public static void updateJob(String job, String group, String cron) {
        Scheduler scheduler = get();
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(job, group);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
            // 重启触发器
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("update job error {}", e.getMessage());
        }
    }

    // ============================

    /**
     * 删除
     */
    public static void deleteJob(String job) {
        deleteJob(job, JOB_GROUP_NAME);
    }

    /**
     * 删除
     *
     * @param job 任务名称
     * @param group 任务组名
     */
    public static void deleteJob(String job, String group) {
        try {
            get().deleteJob(new JobKey(job, group));
        } catch (SchedulerException e) {
            log.error("delete job error {}", e.getMessage());
        }
    }

    // ============================

    /**
     * 暂停
     */
    public static void pauseJob(String job) {
        pauseJob(job, JOB_GROUP_NAME);
    }

    /**
     * 暂停
     *
     * @param job
     * @param group
     */
    public static void pauseJob(String job, String group) {
        Scheduler scheduler = get();
        try {
            JobKey jobKey = JobKey.jobKey(job, group);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            log.error("pause job error {}", e.getMessage());
        }
    }

    // ============================

    /**
     * 恢复
     */
    public static void resumeJob(String job) {
        resumeJob(job, JOB_GROUP_NAME);
    }

    /**
     * 恢复
     *
     * @param job
     * @param group
     */
    public static void resumeJob(String job, String group) {
        Scheduler scheduler = get();
        try {
            JobKey jobKey = JobKey.jobKey(job, group);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            log.error("resume job error {}", e.getMessage());
        }
    }

    // ============================

    /**
     * 立即执行
     *
     */
    public static void runJobNow(String job) {
        resumeJob(job, JOB_GROUP_NAME);
    }

    /**
     * 立即执行
     *
     * @param job
     * @param group
     */
    public static void runJobNow(String job, String group) {
        Scheduler scheduler = get();
        try {
            JobKey jobKey = JobKey.jobKey(job, group);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            log.error("run job error {}", e.getMessage());
        }
    }

    // ============================

    /**
     * 启动任务
     *
     * @throws SchedulerException
     */
    public static void startJobs() {
        Scheduler scheduler = get();
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("start job error {}", e.getMessage());
        }
    }

    /**
     * 关闭任务
     *
     * @throws SchedulerException
     */
    public static void shutdownJobs() {
        Scheduler scheduler = get();
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            log.error("shutdown job error {}", e.getMessage());
        }
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     */
    public static List<Map<String, Object>> queryAllJob() {
        Scheduler scheduler = get();
        List<Map<String, Object>> jobList = Lists.newArrayList();
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Map<String, Object> map = Maps.newConcurrentMap();
                    map.put("key", trigger.getKey());
                    map.put("job", jobKey.getName());
                    map.put("group", jobKey.getGroup());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    map.put("status", triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        map.put("cron", cronExpression);
                    }
                    jobList.add(map);
                }
            }
        } catch (SchedulerException e) {
            log.error("query all job error {}", e.getMessage());
        }
        return jobList;
    }

    /**
     * 获取所有正在运行的job
     *
     * @return
     */
    public static List<Map<String, Object>> queryRunJob() {
        Scheduler scheduler = get();
        List<Map<String, Object>> jobList = Lists.newArrayList();
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            for (JobExecutionContext executingJob : executingJobs) {
                Map<String, Object> map = Maps.newConcurrentMap();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                map.put("key", trigger.getKey());
                map.put("job", jobKey.getName());
                map.put("group", jobKey.getGroup());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                map.put("status", triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    map.put("cron", cronExpression);
                }
                jobList.add(map);
            }
        } catch (SchedulerException e) {
            log.error("query runing job error {}", e.getMessage());
        }
        return jobList;
    }
}
