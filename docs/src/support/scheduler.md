# 任务调度

该功能由 [`QuartzScheduler`](http://www.quartz-scheduler.org/) 封装而来

定时任务几种实现方式

```
1. Java的java.util.Timer类，这个类允许你调度一个java.util.TimerTask任务。
2. Java的线程池类ScheduledExecutorService也可以实现一些简单的定时任务，周期性任务。
3. Spring的@Scheduled，可以将它看成一个轻量级的Quartz，而且使用起来比Quartz简单许多。
4. Quartz是一个功能比较强大的的调度器，可以让你的程序在指定时间执行，也可以按照某一个频度执行,可以方便的分布式部署、便捷的监控和管理任务，适合任务很多的情况。
```

## 依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-quartz</artifactId>
</dependency>
```

## 扩展

```java
public static void addJob(String job, Class<? extends Job> cls, int interval, int repeat)
public static void addJob(String job, Class<? extends Job> cls, int interval, int repeat, Map<? extends String, ? extends Object> data)
public static void addJob(String job, String group, Class<? extends Job> cls, int interval, int repeat, Map<? extends String, ? extends Object> data)
public static void addJob(String job, Class<? extends Job> cls, String cron)
public static void addJob(String job, Class<? extends Job> cls, String cron, Map<? extends String, ? extends Object> data)
public static void addJob(String job, String group, Class<? extends Job> cls, String cron, Map<? extends String, ? extends Object> data)

public static void createJob(String job, Class<? extends Job> cls, ScheduleBuilder<? extends Trigger> ScheduleBuilder, Map<? extends String, ? extends Object> data)
public static void createJob(String job, String group, Class<? extends Job> cls, ScheduleBuilder<? extends Trigger> ScheduleBuilder, Map<? extends String, ? extends Object> data)

public static void updateJob(String job, int interval, int repeat)
public static void updateJob(String job, String cron) }
public static void updateJob(String job, String group, int interval, int repeat)
public static void updateJob(String job, String group, String cron)

public static void deleteJob(String job)
public static void deleteJob(String job, String group)

public static void pauseJob(String job)
public static void pauseJob(String job, String group)

public static void resumeJob(String job)
public static void resumeJob(String job, String group)

public static void runJobNow(String job)
public static void runJobNow(String job, String group)

public static void startJobs()
public static void shutdownJobs()

public static List<Map<String, Object>> queryAllJob()
public static List<Map<String, Object>> queryRunJob()
```

## 示例

- Simple

```java
@Test
public void scheduler_simple() throws InterruptedException {

    Map<String, String> map = Maps.newHashMap();
    map.put("key", "time");

    log.info("【添加A】每1秒输出一次 ");
    ScheduleHelper.addJob(job, SimpleJob.class, 1, 0, map);
    Thread.sleep(6000);

    log.info("【修改A】每2秒输出一次");
    ScheduleHelper.updateJob(job, 2, 0);
    Thread.sleep(6000);

    log.info("【移除A】");
    ScheduleHelper.deleteJob(job);
    Thread.sleep(6000);

    log.info("【添加B】每3秒输出一次");
    ScheduleHelper.addJob(job, SimpleJob.class, 3, 0, map);
    Thread.sleep(6000);

    log.info("【暂停B】");
    ScheduleHelper.pauseJob(job);
    Thread.sleep(6000);

    log.info("【恢复B】");
    ScheduleHelper.resumeJob(job);
    Thread.sleep(6000);

    log.info("【移除B】");
    ScheduleHelper.deleteJob(job);
    Thread.sleep(6000);
}
```

```log
[FastBoot][ INFO][08-22 17:38:21]-->[main: 5490][scheduler_simple(SchedulerHelperTest.java:34)] | - 【添加A】每1秒输出一次
[FastBoot][ INFO][08-22 17:38:21]-->[quartzScheduler_Worker-1: 5514][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:21，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:22]-->[quartzScheduler_Worker-2: 6496][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:22，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:23]-->[quartzScheduler_Worker-3: 7504][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:23，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:24]-->[quartzScheduler_Worker-4: 8507][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:24，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:25]-->[quartzScheduler_Worker-5: 9501][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:25，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:26]-->[quartzScheduler_Worker-6:10497][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:26，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:27]-->[main:11501][scheduler_simple(SchedulerHelperTest.java:38)] | - 【修改A】每2秒输出一次
[FastBoot][ INFO][08-22 17:38:27]-->[quartzScheduler_Worker-7:11501][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:27，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:29]-->[quartzScheduler_Worker-8:13497][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:29，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:31]-->[quartzScheduler_Worker-9:15502][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:31，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:33]-->[quartzScheduler_Worker-10:17502][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:33，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:33]-->[main:17502][scheduler_simple(SchedulerHelperTest.java:42)] | - 【移除A】
[FastBoot][ INFO][08-22 17:38:33]-->[quartzScheduler_Worker-10:17502][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:33，当前任务0个，正在运行 1
[FastBoot][ INFO][08-22 17:38:39]-->[main:23510][scheduler_simple(SchedulerHelperTest.java:46)] | - 【添加B】每3秒输出一次
[FastBoot][ INFO][08-22 17:38:39]-->[quartzScheduler_Worker-1:23514][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:39，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:42]-->[quartzScheduler_Worker-2:26520][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:42，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:45]-->[main:29527][scheduler_simple(SchedulerHelperTest.java:50)] | - 【暂停B】
[FastBoot][ INFO][08-22 17:38:45]-->[quartzScheduler_Worker-3:29528][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:45，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:51]-->[main:35543][scheduler_simple(SchedulerHelperTest.java:54)] | - 【恢复B】
[FastBoot][ INFO][08-22 17:38:51]-->[quartzScheduler_Worker-4:35544][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:51，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:51]-->[quartzScheduler_Worker-5:35544][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:51，当前任务1个，正在运行 2
[FastBoot][ INFO][08-22 17:38:54]-->[quartzScheduler_Worker-6:38514][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:54，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:57]-->[quartzScheduler_Worker-7:41519][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:38:57，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:38:57]-->[main:41550][scheduler_simple(SchedulerHelperTest.java:58)] | - 【移除B】.
```

- Cron

```java
@Test
public void scheduler_cron() throws InterruptedException {

    Map<String, String> map = Maps.newHashMap();
    map.put("key", "time");

    log.info("【添加A】每1秒输出一次 ");
    ScheduleHelper.addJob(job, SimpleJob.class, "0/1 * * * * ?", map);
    Thread.sleep(6000);

    log.info("【修改A】每2秒输出一次");
    ScheduleHelper.updateJob(job, "0/2 * * * * ?");
    Thread.sleep(6000);

    log.info("【移除A】");
    ScheduleHelper.deleteJob(job);
    Thread.sleep(6000);

    log.info("【添加B】每3秒输出一次");
    ScheduleHelper.addJob(job, SimpleJob.class, "*/3 * * * * ?", map);
    Thread.sleep(6000);

    log.info("【暂停B】");
    ScheduleHelper.pauseJob(job);
    Thread.sleep(6000);

    log.info("【恢复B】");
    ScheduleHelper.resumeJob(job);
    Thread.sleep(6000);

    log.info("【移除B】.");
    ScheduleHelper.deleteJob(job);
    Thread.sleep(6000);
}
```

```log
[FastBoot][ INFO][08-22 17:41:53]-->[main: 5185][scheduler_cron(SchedulerHelperTest.java:69)] | - 【添加A】每1秒输出一次
[FastBoot][ INFO][08-22 17:41:53]-->[quartzScheduler_Worker-1: 5220][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:41:53，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:41:54]-->[quartzScheduler_Worker-2: 5694][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:41:54，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:41:55]-->[quartzScheduler_Worker-3: 6701][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:41:55，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:41:56]-->[quartzScheduler_Worker-4: 7705][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:41:56，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:41:57]-->[quartzScheduler_Worker-5: 8696][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:41:57，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:41:58]-->[quartzScheduler_Worker-6: 9701][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:41:58，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:41:59]-->[quartzScheduler_Worker-7:10690][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:41:59，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:41:59]-->[main:11215][scheduler_cron(SchedulerHelperTest.java:73)] | - 【修改A】每2秒输出一次
[FastBoot][ INFO][08-22 17:41:59]-->[quartzScheduler_Worker-8:11218][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:41:59，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:42:00]-->[quartzScheduler_Worker-9:11693][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:42:00，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:42:02]-->[quartzScheduler_Worker-10:13690][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:42:02，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:42:04]-->[quartzScheduler_Worker-1:15692][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:42:04，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:42:05]-->[main:17220][scheduler_cron(SchedulerHelperTest.java:77)] | - 【移除A】
[FastBoot][ INFO][08-22 17:42:11]-->[main:23220][scheduler_cron(SchedulerHelperTest.java:81)] | - 【添加B】每3秒输出一次
[FastBoot][ INFO][08-22 17:42:12]-->[quartzScheduler_Worker-2:23702][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:42:12，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:42:15]-->[quartzScheduler_Worker-3:26702][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:42:15，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:42:17]-->[main:29227][scheduler_cron(SchedulerHelperTest.java:85)] | - 【暂停B】
[FastBoot][ INFO][08-22 17:42:23]-->[main:35230][scheduler_cron(SchedulerHelperTest.java:89)] | - 【恢复B】
[FastBoot][ INFO][08-22 17:42:23]-->[quartzScheduler_Worker-4:35232][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:42:23，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:42:24]-->[quartzScheduler_Worker-5:35696][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:42:24，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:42:27]-->[quartzScheduler_Worker-6:38694][execute(SimpleJob.java:30)] | - simple job time 2020-08-22 17:42:27，当前任务1个，正在运行 1
[FastBoot][ INFO][08-22 17:42:29]-->[main:41246][scheduler_cron(SchedulerHelperTest.java:93)] | - 【移除B】.
```
