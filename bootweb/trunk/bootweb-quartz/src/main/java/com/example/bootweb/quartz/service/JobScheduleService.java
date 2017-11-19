package com.example.bootweb.quartz.service;

import org.quartz.Job;

import com.example.bootweb.quartz.profile.SpringBootPersistQuartz;

@SpringBootPersistQuartz
public interface JobScheduleService {

  void add(Class<? extends Job> jobClass, String jobGroupName, String cronExpression) throws Exception;

  void pause(Class<? extends Job> jobClass, String jobGroupName) throws Exception;

  void resume(Class<? extends Job> jobClass, String jobGroupName) throws Exception;

  void reschedule(Class<? extends Job> jobClass, String jobGroupName, String cronExpression) throws Exception;

  void delete(Class<? extends Job> jobClass, String jobGroupName) throws Exception;

}
