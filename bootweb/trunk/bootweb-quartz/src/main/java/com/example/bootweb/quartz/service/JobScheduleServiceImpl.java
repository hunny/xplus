package com.example.bootweb.quartz.service;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import com.example.bootweb.quartz.profile.SpringBootPersistQuartz;

@SpringBootPersistQuartz
@Service
public class JobScheduleServiceImpl implements JobScheduleService {

  public void add(Class<? extends Job> jobClass, String jobGroupName, String cronExpression) throws Exception {
    // 通过SchedulerFactory获取一个调度器实例
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    // 启动调度器
    sched.start();
    // 构建job信息
    JobDetail jobDetail = JobBuilder //
        .newJob(jobClass) //
        .withIdentity(jobClass.getName(), jobGroupName) //
        .build();
    // 表达式调度构建器(即任务执行的时间)
    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder //
        .cronSchedule(cronExpression);
    // 按新的cronExpression表达式构建一个新的trigger
    CronTrigger trigger = TriggerBuilder //
        .newTrigger() //
        .withIdentity(jobClass.getName(), jobGroupName) //
        .withSchedule(scheduleBuilder) //
        .build();
    try {
      sched.scheduleJob(jobDetail, trigger);
    } catch (SchedulerException e) {
      System.out.println("创建定时任务失败" + e);
      throw new RuntimeException("创建定时任务失败");
    }
  }

  public void pause(Class<? extends Job> jobClass, String jobGroupName) throws Exception {
    // 通过SchedulerFactory获取一个调度器实例
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();
    sched.pauseJob(JobKey.jobKey(jobClass.getName(), jobGroupName));
  }

  public void resume(Class<? extends Job> jobClass, String jobGroupName) throws Exception {
    // 通过SchedulerFactory获取一个调度器实例
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();
    sched.resumeJob(JobKey.jobKey(jobClass.getName(), jobGroupName));
  }

  public void reschedule(Class<? extends Job> jobClass, String jobGroupName, String cronExpression) throws Exception {
    try {
      SchedulerFactory schedulerFactory = new StdSchedulerFactory();
      Scheduler scheduler = schedulerFactory.getScheduler();
      TriggerKey triggerKey = TriggerKey.triggerKey(jobClass.getName(), jobGroupName);
      // 表达式调度构建器
      CronScheduleBuilder scheduleBuilder = CronScheduleBuilder //
          .cronSchedule(cronExpression);
      CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
      // 按新的cronExpression表达式重新构建trigger
      trigger = trigger.getTriggerBuilder() //
          .withIdentity(triggerKey) //
          .withSchedule(scheduleBuilder) //
          .build();
      // 按新的trigger重新设置job执行
      scheduler.rescheduleJob(triggerKey, trigger);
    } catch (SchedulerException e) {
      System.out.println("更新定时任务失败" + e);
      throw new RuntimeException("更新定时任务失败");
    }
  }

  public void delete(Class<? extends Job> jobClass, String jobGroupName) throws Exception {
    // 通过SchedulerFactory获取一个调度器实例
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();
    sched.pauseTrigger(TriggerKey.triggerKey(jobClass.getName(), jobGroupName));
    sched.unscheduleJob(TriggerKey.triggerKey(jobClass.getName(), jobGroupName));
    sched.deleteJob(JobKey.jobKey(jobClass.getName(), jobGroupName));
  }

}
