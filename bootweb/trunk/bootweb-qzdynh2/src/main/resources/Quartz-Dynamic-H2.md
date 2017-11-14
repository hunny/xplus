# SpringBoot Quartz Dynamic Job With H2

## Add Maven Dependency

```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency><!-- 使用内存数据库 -->
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.quartz-scheduler</groupId>
      <artifactId>quartz</artifactId>
    </dependency>
    <dependency><!-- 该依赖必加，里面有sping对schedule的支持 -->
      <groupId>org.springframework</groupId>
      <artifactId>spring-context-support</artifactId>
    </dependency>
```

## Add Application Property `application.properties`

```
#是否生成ddl(Data Definition Language)语句
spring.jpa.generate-ddl=false

#是否打印sql语句
spring.jpa.show-sql=true

#自动生成ddl，由于指定了具体的ddl，此处设置为none
spring.jpa.hibernate.ddl-auto=none

#使用H2数据库
spring.datasource.platform=h2

#指定生成数据库的schema文件位置
spring.datasource.schema=classpath:schema.sql

#指定插入数据库语句的脚本位置
spring.datasource.data=classpath:data.sql

#配置日志打印信息
logging.level.root=INFO
logging.level.org.hibernate=INFO
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
logging.level.org.hibernate.type.descriptor.sql.BasicExtractor=TRACE
logging.level.com.example.bootweb.qzdynh2=DEBUG
```

## Add Jpa Entity

```
package com.example.bootweb.qzdynh2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class QuartzConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String cron;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCron() {
    return cron;
  }

  public void setCron(String cron) {
    this.cron = cron;
  }

}

```

## Add Quartz Framework Configuration

```
package com.example.bootweb.qzdynh2.config;

import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.example.bootweb.qzdynh2.task.ScheduleTask;

@Configuration
public class QuartzConfigration {

  /**
   * @param task
   *          ScheduleTask为用户定义的需要执行的任务
   */
  @Bean(name = "jobDetail")
  public MethodInvokingJobDetailFactoryBean detailFactoryBean(ScheduleTask task) {//
    MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();

    // 是否并发执行 例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
    // 如果此处为true，则下一个任务会执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行
    jobDetail.setConcurrent(false);

    jobDetail.setName("myjobname");// 设置任务的名字
    jobDetail.setGroup("myjobgroup");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用

    // 为需要执行的实体类对应的对象
    jobDetail.setTargetObject(task);
    // sayHello为需要执行的方法
    // 通过这几个配置，告诉JobDetailFactoryBean我们需要执行定时执行ScheduleTask类中的sayHello方法
    jobDetail.setTargetMethod("sayHello");

    return jobDetail;
  }

  /**
   * @param jobDetail
   *          配置定时任务的触发器，也就是什么时候触发执行定时任务
   */
  @Bean(name = "jobTrigger")
  public CronTriggerFactoryBean cronJobTrigger(MethodInvokingJobDetailFactoryBean jobDetail) {
    CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
    tigger.setJobDetail(jobDetail.getObject());
    tigger.setCronExpression("0 30 13 * * ?");// 初始时的cron表达式
    tigger.setName("myjobname");// trigger的name
    return tigger;
  }

  /**
   * 定义quartz调度工厂
   * 
   * @param cronJobTrigger
   */
  @Bean(name = "scheduler")
  public SchedulerFactoryBean schedulerFactory(Trigger cronJobTrigger) {
    SchedulerFactoryBean bean = new SchedulerFactoryBean();
    // 用于quartz集群, QuartzScheduler 启动时更新己存在的Job
    bean.setOverwriteExistingJobs(true);
    // 延时启动，应用启动1秒后
    bean.setStartupDelay(1);
    // 注册触发器
    bean.setTriggers(cronJobTrigger);
    return bean;
  }

}
```

## Add Application Customer Task Scheduled

```
package com.example.bootweb.qzdynh2.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component // 此注解必加
@EnableScheduling // 此注解必加
public class ScheduleTask {

  private final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

  public void sayHello() {

    logger.info("Hello world, I'm the king of the world!!!");

  }

}

```

## Add Jpa Database Access Crud Interface

```
package com.example.bootweb.qzdynh2.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.bootweb.qzdynh2.entity.QuartzConfig;

public interface QuartzConfigRepository extends CrudRepository<QuartzConfig, Long> {

}
```

## Add Schedule to Query Database for trigger Job Task.

```
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
``` 



