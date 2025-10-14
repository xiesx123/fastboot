# 任务调度

基于 [`QuartzScheduler`](http://www.quartz-scheduler.org/) 扩展，封装简化操作

## 依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-quartz</artifactId>
</dependency>
```

## 扩展

```java
// 重复间隔任务
public static void addJob(String job, Class<? extends Job> cls, int interval, int repeat)
public static void addJob(String job, Class<? extends Job> cls, int interval, int repeat, Map<? extends String, ? extends Object> data)
public static void addJob(String job, String group, Class<? extends Job> cls, int interval, int repeat, Map<? extends String, ? extends Object> data)
// cron任务
public static void addJob(String job, Class<? extends Job> cls, String cron)
public static void addJob(String job, Class<? extends Job> cls, String cron, Map<? extends String, ? extends Object> data)
public static void addJob(String job, String group, Class<? extends Job> cls, String cron, Map<? extends String, ? extends Object> data)
// 创建
public static void createJob(String job, Class<? extends Job> cls, ScheduleBuilder<? extends Trigger> ScheduleBuilder, Map<? extends String, ? extends Object> data)
public static void createJob(String job, String group, Class<? extends Job> cls, ScheduleBuilder<? extends Trigger> ScheduleBuilder, Map<? extends String, ? extends Object> data)
// 修改
public static void updateJob(String job, int interval, int repeat)
public static void updateJob(String job, String cron) }
public static void updateJob(String job, String group, int interval, int repeat)
public static void updateJob(String job, String group, String cron)
// 删除
public static void deleteJob(String job)
public static void deleteJob(String job, String group)
// 暂停
public static void pauseJob(String job)
public static void pauseJob(String job, String group)
// 恢复
public static void resumeJob(String job)
public static void resumeJob(String job, String group)
// 立即执行
public static void runJobNow(String job)
public static void runJobNow(String job, String group)
// 启停
public static void startJobs()
public static void shutdownJobs()
// 查询
public static List<Map<String, Object>> queryAllJob()
public static List<Map<String, Object>> queryRunJob()
```

## 示例

```java
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
```

- Simple

```java
@Test
@Order(1)
public void simple() {

    String job = TimeJob.class.getSimpleName();
    Map<String, Object> map = Maps.newConcurrentMap();
    map.put(TimeJob.FIELDS.job, "simple");
    map.put(TimeJob.FIELDS.time, "time");

    log.info("【添加A】每1秒输出一次 ");
    ScheduleHelper.addJob(TimeJob.class, 1, 0, map);
    ThreadUtil.sleep(2000);

    log.info("【修改A】每2秒输出一次");
    ScheduleHelper.updateJob(job, 2, 0);
    ThreadUtil.sleep(4000);

    log.info("【移除A】");
    ScheduleHelper.deleteJob(job);
    ThreadUtil.sleep(1000);

    log.info("【添加B】每3秒输出一次");
    ScheduleHelper.addJob(TimeJob.class, 3, 0, map);
    ThreadUtil.sleep(6000);

    log.info("【暂停B】");
    ScheduleHelper.pauseJob(job);
    ThreadUtil.sleep(2000);

    log.info("【恢复B】");
    ScheduleHelper.resumeJob(job);
    ThreadUtil.sleep(2000);

    log.info("【移除B】.");
    ScheduleHelper.deleteJob(job);
    ThreadUtil.sleep(1000);
}
```

```log
2025-10-14 18:29:31 INFO main:3925 SchedulerTest.java:33 - 【添加A】每1秒输出一次 
2025-10-14 18:29:31 INFO quartzScheduler_Worker-1:3932 TimeJob.java:24 - simple job time，当前任务1个，正在运行 1
2025-10-14 18:29:32 INFO quartzScheduler_Worker-2:4927 TimeJob.java:24 - simple job time，当前任务1个，正在运行 1
2025-10-14 18:29:33 INFO quartzScheduler_Worker-3:5928 TimeJob.java:24 - simple job time，当前任务1个，正在运行 1
2025-10-14 18:29:33 INFO main:5943 SchedulerTest.java:37 - 【修改A】每2秒输出一次
2025-10-14 18:29:33 INFO quartzScheduler_Worker-4:5944 TimeJob.java:24 - simple job time，当前任务1个，正在运行 1
2025-10-14 18:29:33 INFO quartzScheduler_Worker-5:5944 TimeJob.java:24 - simple job time，当前任务1个，正在运行 2
2025-10-14 18:29:35 INFO quartzScheduler_Worker-6:7928 TimeJob.java:24 - simple job time，当前任务1个，正在运行 1
2025-10-14 18:29:37 INFO quartzScheduler_Worker-7:9932 TimeJob.java:24 - simple job time，当前任务1个，正在运行 1
2025-10-14 18:29:37 INFO main:9946 SchedulerTest.java:41 - 【移除A】
2025-10-14 18:29:38 INFO main:10953 SchedulerTest.java:45 - 【添加B】每3秒输出一次
2025-10-14 18:29:38 INFO quartzScheduler_Worker-8:10955 TimeJob.java:24 - simple job time，当前任务1个，正在运行 1
2025-10-14 18:29:41 INFO quartzScheduler_Worker-9:13955 TimeJob.java:24 - simple job time，当前任务1个，正在运行 1
2025-10-14 18:29:44 INFO main:16962 SchedulerTest.java:49 - 【暂停B】
2025-10-14 18:29:44 INFO quartzScheduler_Worker-10:16963 TimeJob.java:24 - simple job time，当前任务1个，正在运行 1
2025-10-14 18:29:46 INFO main:18972 SchedulerTest.java:53 - 【恢复B】
2025-10-14 18:29:47 INFO quartzScheduler_Worker-1:19955 TimeJob.java:24 - simple job time，当前任务1个，正在运行 1
2025-10-14 18:29:48 INFO main:20986 SchedulerTest.java:57 - 【移除B】.
```

- Cron

```java
@Test
@Order(2)
public void cron() {

    String job = TimeJob.class.getSimpleName();
    Map<String, Object> map = Maps.newConcurrentMap();
    map.put(TimeJob.FIELDS.job, "job");
    map.put(TimeJob.FIELDS.time, "time");

    log.info("【添加A】每1秒输出一次 ");
    ScheduleHelper.addJob(job, job_group, TimeJob.class, "0/1 * * * * ?", map);
    ThreadUtil.sleep(2000);

    log.info("【修改A】每2秒输出一次");
    ScheduleHelper.updateJob(job, "0/2 * * * * ?");
    ThreadUtil.sleep(4000);

    log.info("【移除A】");
    ScheduleHelper.deleteJob(job);
    ThreadUtil.sleep(1000);

    log.info("【添加B】每3秒输出一次");
    ScheduleHelper.addJob(job, job_group, TimeJob.class, "*/3 * * * * ?", map);
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
```

```log
2025-10-14 18:30:30 INFO main:4001 SchedulerTest.java:71 - 【添加A】每1秒输出一次 
2025-10-14 18:30:30 INFO quartzScheduler_Worker-1:4009 TimeJob.java:24 - cron job time，当前任务1个，正在运行 1
2025-10-14 18:30:31 INFO quartzScheduler_Worker-2:4758 TimeJob.java:24 - cron job time，当前任务1个，正在运行 1
2025-10-14 18:30:32 INFO quartzScheduler_Worker-3:5758 TimeJob.java:24 - cron job time，当前任务1个，正在运行 1
2025-10-14 18:30:32 INFO main:6006 SchedulerTest.java:75 - 【修改A】每2秒输出一次
2025-10-14 18:30:32 INFO quartzScheduler_Worker-5:6010 TimeJob.java:24 - cron job time，当前任务1个，正在运行 2
2025-10-14 18:30:32 INFO quartzScheduler_Worker-4:6010 TimeJob.java:24 - cron job time，当前任务1个，正在运行 1
2025-10-14 18:30:34 INFO quartzScheduler_Worker-6:7750 TimeJob.java:24 - cron job time，当前任务1个，正在运行 1
2025-10-14 18:30:36 INFO quartzScheduler_Worker-7:9749 TimeJob.java:24 - cron job time，当前任务1个，正在运行 1
2025-10-14 18:30:36 INFO main:10009 SchedulerTest.java:79 - 【移除A】
2025-10-14 18:30:37 INFO main:11020 SchedulerTest.java:83 - 【添加B】每3秒输出一次
2025-10-14 18:30:39 INFO quartzScheduler_Worker-8:12751 TimeJob.java:24 - cron job time，当前任务1个，正在运行 1
2025-10-14 18:30:42 INFO quartzScheduler_Worker-9:15749 TimeJob.java:24 - cron job time，当前任务1个，正在运行 1
2025-10-14 18:30:43 INFO main:17032 SchedulerTest.java:87 - 【暂停B】
2025-10-14 18:30:45 INFO main:19045 SchedulerTest.java:91 - 【恢复B】
2025-10-14 18:30:45 INFO quartzScheduler_Worker-10:19048 TimeJob.java:24 - cron job time，当前任务1个，正在运行 1
2025-10-14 18:30:47 INFO main:21048 SchedulerTest.java:95 - 【移除B】
```
