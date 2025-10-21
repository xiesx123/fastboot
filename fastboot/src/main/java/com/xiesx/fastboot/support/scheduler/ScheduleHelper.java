package com.xiesx.fastboot.support.scheduler;

import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xiesx.fastboot.SpringHelper;
import com.xiesx.fastboot.core.exception.RunExc;
import com.xiesx.fastboot.core.exception.RunException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

@Log4j2
public class ScheduleHelper {

  private static String JOB_GROUP_NAME = "FAST_JOB_GROUP_NAME";

  private static Scheduler scheduler;

  public static Scheduler get() {
    if (scheduler == null) {
      try {
        scheduler = SpringHelper.getBean(Scheduler.class);
      } catch (Exception e) {
        new RunException(
            RunExc.RUNTIME,
            " validation dependency is missing. Please add 'spring-boot-starter-validation' to "
                + " your pom.xml.");
      }
    }
    return scheduler;
  }

  // ============================

  /** 增加 simpleJob */
  public static void addJob(Class<? extends Job> cls, int interval, int repeat) {
    // 创建
    addJob(cls.getSimpleName(), JOB_GROUP_NAME, cls, interval, repeat, null);
  }

  public static void addJob(
      Class<? extends Job> cls,
      int interval,
      int repeat,
      Map<? extends String, ? extends Object> data) {
    // 创建
    addJob(cls.getSimpleName(), JOB_GROUP_NAME, cls, interval, repeat, data);
  }

  public static void addJob(
      String job,
      String group,
      Class<? extends Job> cls,
      int interval,
      int repeat,
      Map<? extends String, ? extends Object> data) {
    // 构建SimpleScheduleBuilder规则
    SimpleScheduleBuilder simpleBuilder =
        SimpleScheduleBuilder.simpleSchedule()
            // 几秒钟重复执行
            .withIntervalInSeconds(interval);
    if (repeat > 0) {
      // 重复次数
      simpleBuilder.withRepeatCount(repeat);
    }
    // 一直执行
    simpleBuilder.repeatForever();
    // 创建
    createJob(job, group, cls, simpleBuilder, data);
  }

  /** 增加 cronJob */
  public static void addJob(Class<? extends Job> cls, String cron) {
    addJob(cls.getSimpleName(), JOB_GROUP_NAME, cls, cron, null);
  }

  public static void addJob(
      Class<? extends Job> cls, String cron, Map<? extends String, ? extends Object> data) {
    addJob(cls.getSimpleName(), JOB_GROUP_NAME, cls, cron, data);
  }

  public static void addJob(
      String job,
      String group,
      Class<? extends Job> cls,
      String cron,
      Map<? extends String, ? extends Object> data) {
    // 构建CronScheduleBuilder规则
    CronScheduleBuilder cronBuilder = CronScheduleBuilder.cronSchedule(cron);
    // 创建
    createJob(job, group, cls, cronBuilder, data);
  }

  // ============================

  /** 创建 */
  public static void createJob(
      String job,
      Class<? extends Job> cls,
      ScheduleBuilder<? extends Trigger> schedule,
      Map<? extends String, ? extends Object> data) {
    createJob(job, JOB_GROUP_NAME, cls, schedule, data);
  }

  public static void createJob(
      String job,
      String group,
      Class<? extends Job> cls,
      ScheduleBuilder<? extends Trigger> schedule,
      Map<? extends String, ? extends Object> data) {
    try {
      // 构建实例
      JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(job, group).build();
      // 设置参数
      if (MapUtil.isNotEmpty(data)) {
        jobDetail.getJobDataMap().putAll(data);
      }
      // 构建触发器
      TriggerBuilder<Trigger> trigger = TriggerBuilder.newTrigger().withIdentity(job, group);
      trigger.withSchedule(schedule);
      trigger.startNow();
      // 判断是否存在
      Scheduler scheduler = get();
      if (!scheduler.checkExists(jobDetail.getKey())) {
        scheduler.scheduleJob(jobDetail, trigger.build());
      }
    } catch (SchedulerException e) {
      log.error("create job error {}", e.getMessage());
    }
  }

  // ============================

  /** 修改 */
  public static void updateJob(String job, int interval, int repeat) {
    updateJob(job, JOB_GROUP_NAME, interval, repeat);
  }

  public static void updateJob(String job, String cron) {
    updateJob(job, JOB_GROUP_NAME, cron);
  }

  public static void updateJob(String job, String group, int interval, int repeat) {
    Scheduler scheduler = get();
    try {
      TriggerKey triggerKey = TriggerKey.triggerKey(job, group);
      SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
      //
      SimpleScheduleBuilder simpleBuilder =
          SimpleScheduleBuilder.simpleSchedule() // 几秒钟重复执行
              .withIntervalInSeconds(interval);
      if (repeat > 0) {
        // 重复次数
        simpleBuilder.withRepeatCount(repeat);
      }
      // 一直执行
      simpleBuilder.repeatForever();
      trigger =
          trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(simpleBuilder).build();
      // 重启触发器
      scheduler.rescheduleJob(triggerKey, trigger);
    } catch (SchedulerException e) {
      log.error("update job error {}", e.getMessage());
    }
  }

  public static void updateJob(String job, String group, String cron) {
    Scheduler scheduler = get();
    try {
      TriggerKey triggerKey = TriggerKey.triggerKey(job, group);
      CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
      //
      trigger =
          trigger
              .getTriggerBuilder()
              .withIdentity(triggerKey)
              .withSchedule(CronScheduleBuilder.cronSchedule(cron))
              .build();
      // 重启触发器
      scheduler.rescheduleJob(triggerKey, trigger);
    } catch (SchedulerException e) {
      log.error("update job error {}", e.getMessage());
    }
  }

  // ============================

  /** 删除 */
  public static void deleteJob(String job) {
    deleteJob(job, JOB_GROUP_NAME);
  }

  public static void deleteJob(String job, String group) {
    try {
      get().deleteJob(new JobKey(job, group));
    } catch (SchedulerException e) {
      log.error("delete job error {}", e.getMessage());
    }
  }

  // ============================

  /** 暂停 */
  public static void pauseJob(String job) {
    pauseJob(job, JOB_GROUP_NAME);
  }

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

  /** 恢复 */
  public static void resumeJob(String job) {
    resumeJob(job, JOB_GROUP_NAME);
  }

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

  /** 立即执行 */
  public static void runJobNow(String job) {
    resumeJob(job, JOB_GROUP_NAME);
  }

  /** 立即执行 */
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

  /** 启动任务 */
  public static void startJobs() {
    Scheduler scheduler = get();
    try {
      scheduler.start();
    } catch (SchedulerException e) {
      log.error("start job error {}", e.getMessage());
    }
  }

  /** 关闭任务 */
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

  /** 是否存在 */
  public static boolean checkExists(JobKey jobKey) {
    Scheduler scheduler = get();
    try {
      return scheduler.checkExists(jobKey);
    } catch (SchedulerException e) {
      log.error("check job exists error {}", e.getMessage());
    }
    return false;
  }

  /** 获取所有计划中的任务列表 */
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

  /** 获取所有正在运行的job */
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
