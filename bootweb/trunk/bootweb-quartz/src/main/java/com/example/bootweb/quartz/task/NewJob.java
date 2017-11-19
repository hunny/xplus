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
