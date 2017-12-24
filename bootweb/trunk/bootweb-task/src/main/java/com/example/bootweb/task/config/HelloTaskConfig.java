package com.example.bootweb.task.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.listener.TaskExecutionListener;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.listener.annotation.FailedTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.bootweb.task.profile.HelloTaskProfile;

@Configuration
@EnableTask
@HelloTaskProfile
public class HelloTaskConfig {

  private final Logger logger = LoggerFactory.getLogger(HelloTaskConfig.class);

  @Bean
  public CommandLineRunner commandLineRunner() {
    return strings -> {
      logger.info("Hello, Task! Run my command line.");
      System.out.println("Hello, Task! Executed at :" + new SimpleDateFormat().format(new Date()));
    };
  }
  
  @Bean 
  public TaskExecutionListener taskExecutionListener() {
    return new HelloTaskExecutionListener();
  }
  
  @BeforeTask
  public void onTaskStartup(TaskExecution arg0) {
    logger.info("BeforeTask.");
  }
  
  @AfterTask
  public void onTaskEnd(TaskExecution arg0) {
    logger.info("AfterTask.");
  }

  @FailedTask
  public void onTaskFailed(TaskExecution arg0, Throwable arg1) {
    logger.info("Failed Task.");
  }

}
