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
