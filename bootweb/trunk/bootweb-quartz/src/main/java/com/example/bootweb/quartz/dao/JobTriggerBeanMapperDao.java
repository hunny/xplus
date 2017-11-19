package com.example.bootweb.quartz.dao;

import java.util.List;

import com.example.bootweb.quartz.entity.JobTriggerBean;
import com.example.bootweb.quartz.profile.SpringBootPersistQuartzMybatis;

@SpringBootPersistQuartzMybatis
public interface JobTriggerBeanMapperDao {

  public List<JobTriggerBean> listJobTrigger();
  
}
