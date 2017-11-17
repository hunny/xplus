package com.example.bootweb.qzdynh2.config;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.bootweb.qzdynh2.dao.QuartzConfigRepository;

@EnableScheduling
@Component
public class ScheduleRefreshDatabase {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private QuartzConfigRepository quartzConfigRepository;

  @Resource(name = "jobDetail")
  private JobDetail jobDetail;

  @Resource(name = "jobTrigger")
  private CronTrigger cronTrigger;

  @Resource(name = "scheduler")
  private Scheduler scheduler;

  @Scheduled(fixedRate = 5000) // 每隔5s查库，并根据查询结果决定是否重新设置定时任务
  public void scheduleUpdateCronTrigger() throws SchedulerException {
    CronTrigger trigger = (CronTrigger) scheduler.getTrigger(cronTrigger.getKey());
    String runningCron = trigger.getCronExpression();// 当前Trigger使用的
    String databaseCron = quartzConfigRepository.findOne(1L).getCron();// 从数据库查询出来的
    logger.debug("current cron: {}", runningCron);
    logger.debug("database cron: {}", databaseCron);
    if (runningCron.equals(databaseCron)) {
      // 如果当前使用的cron表达式和从数据库中查询出来的cron表达式一致，则不刷新任务
    } else {
      // 表达式调度构建器
      CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(databaseCron);
      // 按新的cronExpression表达式重新构建trigger
      trigger = (CronTrigger) scheduler.getTrigger(cronTrigger.getKey());
      trigger = trigger.getTriggerBuilder().withIdentity(cronTrigger.getKey())
          .withSchedule(scheduleBuilder).build();
      // 按新的trigger重新设置job执行
      scheduler.rescheduleJob(cronTrigger.getKey(), trigger);
      runningCron = databaseCron;
    }

  }
}
