# SpringBoot persist Quartz using H2

 * 演示Quartz持久化数据到H2数据库。
 * 运行时配置`spring.profiles.active=SpringBootPersistQuartz`时使用。

## application-SpringBootPersistQuartz.yml

```yml
spring:
  datasource:
    platform: h2
    url: jdbc:h2:mem:testdb
    username: sa
    password: 
    driver-class-name: org.h2.Driver
    schema: classpath:schema-quartz.sql
    data:
  h2:
    console:
      enabled: true
      settings:
        web-allow-others: true
        trace: true
      path: /h2

logging:
  level:
    com:
      example:
        bootweb:
          qzdynh2: DEBUG
```

## quartz-SpringBootPersistQuartz.properties

```properties
# 固定前缀org.quartz
# 主要分为scheduler、threadPool、jobStore、plugin等部分
org.quartz.scheduler.instanceName = DefaultQuartzScheduler
org.quartz.scheduler.rmi.export = false
org.quartz.scheduler.rmi.proxy = false
org.quartz.scheduler.wrapJobExecutionInUserTransaction = false

# 实例化ThreadPool时，使用的线程类为SimpleThreadPool
org.quartz.threadPool.class = org.quartz.simpl.SimpleThreadPool

# threadCount和threadPriority将以setter的形式注入ThreadPool实例
# 并发个数
org.quartz.threadPool.threadCount = 5
# 优先级
org.quartz.threadPool.threadPriority = 5
org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread = true
org.quartz.jobStore.misfireThreshold = 5000

# 默认存储在内存中
#org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore
org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate

#持久化
org.quartz.jobStore.class = org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.tablePrefix = QRTZ_
org.quartz.jobStore.dataSource = qzDS
org.quartz.dataSource.qzDS.driver = org.h2.Driver
org.quartz.dataSource.qzDS.URL = jdbc:h2:mem:testdb
org.quartz.dataSource.qzDS.user = sa
org.quartz.dataSource.qzDS.password = 
org.quartz.dataSource.qzDS.maxConnections = 10
```

## Init Quartz Database schema-quartz.sql

* 各环境下的sql在包`org.quartz.impl.jdbcjobstore`下。

```sql
-- Thanks to Amir Kibbar and Peter Rietzler for contributing the schema for H2 database, 
-- and verifying that it works with Quartz's StdJDBCDelegate
--
-- Note, Quartz depends on row-level locking which means you must use the MVCC=TRUE 
-- setting on your H2 database, or you will experience dead-locks
--
--
-- In your Quartz properties file, you'll need to set 
-- org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate

CREATE TABLE QRTZ_CALENDARS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  CALENDAR_NAME VARCHAR (200)  NOT NULL ,
  CALENDAR IMAGE NOT NULL
);

CREATE TABLE QRTZ_CRON_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR (200)  NOT NULL ,
  TRIGGER_GROUP VARCHAR (200)  NOT NULL ,
  CRON_EXPRESSION VARCHAR (120)  NOT NULL ,
  TIME_ZONE_ID VARCHAR (80) 
);

CREATE TABLE QRTZ_FIRED_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  ENTRY_ID VARCHAR (95)  NOT NULL ,
  TRIGGER_NAME VARCHAR (200)  NOT NULL ,
  TRIGGER_GROUP VARCHAR (200)  NOT NULL ,
  INSTANCE_NAME VARCHAR (200)  NOT NULL ,
  FIRED_TIME BIGINT NOT NULL ,
  SCHED_TIME BIGINT NOT NULL ,
  PRIORITY INTEGER NOT NULL ,
  STATE VARCHAR (16)  NOT NULL,
  JOB_NAME VARCHAR (200)  NULL ,
  JOB_GROUP VARCHAR (200)  NULL ,
  IS_NONCONCURRENT BOOLEAN  NULL ,
  REQUESTS_RECOVERY BOOLEAN  NULL 
);

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_GROUP VARCHAR (200)  NOT NULL 
);

CREATE TABLE QRTZ_SCHEDULER_STATE (
  SCHED_NAME VARCHAR(120) NOT NULL,
  INSTANCE_NAME VARCHAR (200)  NOT NULL ,
  LAST_CHECKIN_TIME BIGINT NOT NULL ,
  CHECKIN_INTERVAL BIGINT NOT NULL
);

CREATE TABLE QRTZ_LOCKS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  LOCK_NAME VARCHAR (40)  NOT NULL 
);

CREATE TABLE QRTZ_JOB_DETAILS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  JOB_NAME VARCHAR (200)  NOT NULL ,
  JOB_GROUP VARCHAR (200)  NOT NULL ,
  DESCRIPTION VARCHAR (250) NULL ,
  JOB_CLASS_NAME VARCHAR (250)  NOT NULL ,
  IS_DURABLE BOOLEAN  NOT NULL ,
  IS_NONCONCURRENT BOOLEAN  NOT NULL ,
  IS_UPDATE_DATA BOOLEAN  NOT NULL ,
  REQUESTS_RECOVERY BOOLEAN  NOT NULL ,
  JOB_DATA IMAGE NULL
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR (200)  NOT NULL ,
  TRIGGER_GROUP VARCHAR (200)  NOT NULL ,
  REPEAT_COUNT BIGINT NOT NULL ,
  REPEAT_INTERVAL BIGINT NOT NULL ,
  TIMES_TRIGGERED BIGINT NOT NULL
);

CREATE TABLE qrtz_simprop_triggers
  (          
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INTEGER NULL,
    INT_PROP_2 INTEGER NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 BOOLEAN NULL,
    BOOL_PROP_2 BOOLEAN NULL,
);

CREATE TABLE QRTZ_BLOB_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR (200)  NOT NULL ,
  TRIGGER_GROUP VARCHAR (200)  NOT NULL ,
  BLOB_DATA IMAGE NULL
);

CREATE TABLE QRTZ_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR (200)  NOT NULL ,
  TRIGGER_GROUP VARCHAR (200)  NOT NULL ,
  JOB_NAME VARCHAR (200)  NOT NULL ,
  JOB_GROUP VARCHAR (200)  NOT NULL ,
  DESCRIPTION VARCHAR (250) NULL ,
  NEXT_FIRE_TIME BIGINT NULL ,
  PREV_FIRE_TIME BIGINT NULL ,
  PRIORITY INTEGER NULL ,
  TRIGGER_STATE VARCHAR (16)  NOT NULL ,
  TRIGGER_TYPE VARCHAR (8)  NOT NULL ,
  START_TIME BIGINT NOT NULL ,
  END_TIME BIGINT NULL ,
  CALENDAR_NAME VARCHAR (200)  NULL ,
  MISFIRE_INSTR SMALLINT NULL ,
  JOB_DATA IMAGE NULL
);

ALTER TABLE QRTZ_CALENDARS  ADD
  CONSTRAINT PK_QRTZ_CALENDARS PRIMARY KEY  
  (
    SCHED_NAME,
    CALENDAR_NAME
  );

ALTER TABLE QRTZ_CRON_TRIGGERS  ADD
  CONSTRAINT PK_QRTZ_CRON_TRIGGERS PRIMARY KEY  
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  );

ALTER TABLE QRTZ_FIRED_TRIGGERS  ADD
  CONSTRAINT PK_QRTZ_FIRED_TRIGGERS PRIMARY KEY  
  (
    SCHED_NAME,
    ENTRY_ID
  );

ALTER TABLE QRTZ_PAUSED_TRIGGER_GRPS  ADD
  CONSTRAINT PK_QRTZ_PAUSED_TRIGGER_GRPS PRIMARY KEY  
  (
    SCHED_NAME,
    TRIGGER_GROUP
  );

ALTER TABLE QRTZ_SCHEDULER_STATE  ADD
  CONSTRAINT PK_QRTZ_SCHEDULER_STATE PRIMARY KEY  
  (
    SCHED_NAME,
    INSTANCE_NAME
  );

ALTER TABLE QRTZ_LOCKS  ADD
  CONSTRAINT PK_QRTZ_LOCKS PRIMARY KEY  
  (
    SCHED_NAME,
    LOCK_NAME
  );

ALTER TABLE QRTZ_JOB_DETAILS  ADD
  CONSTRAINT PK_QRTZ_JOB_DETAILS PRIMARY KEY  
  (
    SCHED_NAME,
    JOB_NAME,
    JOB_GROUP
  );

ALTER TABLE QRTZ_SIMPLE_TRIGGERS  ADD
  CONSTRAINT PK_QRTZ_SIMPLE_TRIGGERS PRIMARY KEY  
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  );

ALTER TABLE QRTZ_SIMPROP_TRIGGERS  ADD
  CONSTRAINT PK_QRTZ_SIMPROP_TRIGGERS PRIMARY KEY  
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  );

ALTER TABLE QRTZ_TRIGGERS  ADD
  CONSTRAINT PK_QRTZ_TRIGGERS PRIMARY KEY  
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  );

ALTER TABLE QRTZ_CRON_TRIGGERS ADD
  CONSTRAINT FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS FOREIGN KEY
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  ) REFERENCES QRTZ_TRIGGERS (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  ) ON DELETE CASCADE;


ALTER TABLE QRTZ_SIMPLE_TRIGGERS ADD
  CONSTRAINT FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS FOREIGN KEY
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  ) REFERENCES QRTZ_TRIGGERS (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  ) ON DELETE CASCADE;

ALTER TABLE QRTZ_SIMPROP_TRIGGERS ADD
  CONSTRAINT FK_QRTZ_SIMPROP_TRIGGERS_QRTZ_TRIGGERS FOREIGN KEY
  (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  ) REFERENCES QRTZ_TRIGGERS (
    SCHED_NAME,
    TRIGGER_NAME,
    TRIGGER_GROUP
  ) ON DELETE CASCADE;


ALTER TABLE QRTZ_TRIGGERS ADD
  CONSTRAINT FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS FOREIGN KEY
  (
    SCHED_NAME,
    JOB_NAME,
    JOB_GROUP
  ) REFERENCES QRTZ_JOB_DETAILS (
    SCHED_NAME,
    JOB_NAME,
    JOB_GROUP
  );
  
COMMIT;
```

## SchedulerConfig

```java
package com.example.bootweb.quartz.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.example.bootweb.quartz.profile.SpringBootPersistQuartz;

@Configuration
@SpringBootPersistQuartz
public class SchedulerConfig {
  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) throws IOException {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();
    factory.setQuartzProperties(quartzProperties());
    return factory;
  }
  @Bean
  public Properties quartzProperties() throws IOException {
    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean.setLocation(new ClassPathResource("/quartz-SpringBootPersistQuartz.properties"));
    return propertiesFactoryBean.getObject();
  }
  /**
   * 监听到工程的启动，在工程停止再启动时可以让已有的定时任务继续进行。
   * @return
   */
  @Bean
  public QuartzInitializerListener executorListener() {
    return new QuartzInitializerListener();
  }
}
```

## JobScheduleServiceImpl

```java
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
```

## HelloJob & NewJob

```java
package com.example.bootweb.quartz.task;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.bootweb.quartz.profile.SpringBootPersistQuartz;

@SpringBootPersistQuartz
public class HelloJob implements Job {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Override
  public void execute(JobExecutionContext arg0) throws JobExecutionException {
    logger.info("Hello Job执行时间: " + new Date());
  }

}
```

```java
package com.example.bootweb.quartz.task;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.bootweb.quartz.profile.SpringBootPersistQuartz;

@SpringBootPersistQuartz
public class NewJob implements Job {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Override
  public void execute(JobExecutionContext arg0) throws JobExecutionException {
    logger.info("New Job执行时间: " + new Date());
  }

}
```

## Startup to schedule `ApplicationRunAtStartup`

```java
package com.example.bootweb.quartz.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import com.example.bootweb.quartz.profile.SpringBootPersistQuartz;
import com.example.bootweb.quartz.service.JobScheduleService;
import com.example.bootweb.quartz.task.HelloJob;

@Configuration
@SpringBootPersistQuartz
public class ApplicationRunAtStartup {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Bean
  public ApplicationListener<ContextRefreshedEvent> abc() {
    return new ApplicationListener<ContextRefreshedEvent>() {
      @Override
      public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("启动时执行函数。");
        JobScheduleService jobScheduleService = event //
            .getApplicationContext() //
            .getBean(JobScheduleService.class);
        try {
          jobScheduleService.add(HelloJob.class, "test", "0/5 * * * * ? ");
        } catch (Exception e) {
          e.printStackTrace();
          logger.info("启动时执行函数:{}", e.getMessage());
        }
      }
    };
  }
}
```



