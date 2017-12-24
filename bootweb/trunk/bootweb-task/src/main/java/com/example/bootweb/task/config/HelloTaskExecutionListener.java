package com.example.bootweb.task.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.task.listener.TaskExecutionListener;
import org.springframework.cloud.task.repository.TaskExecution;

import com.example.bootweb.task.profile.HelloTaskProfile;

@HelloTaskProfile
public class HelloTaskExecutionListener implements TaskExecutionListener {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Override
  public void onTaskEnd(TaskExecution arg0) {
    logger.info("Task End.");
  }

  @Override
  public void onTaskFailed(TaskExecution arg0, Throwable arg1) {
    logger.info("Task Failed.");
  }

  @Override
  public void onTaskStartup(TaskExecution arg0) {
    logger.info("Task Startup.");
  }

}
