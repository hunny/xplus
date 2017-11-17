package com.example.bootweb.quartz.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.bootweb.quartz.entity.QuartzConfig;
import com.example.bootweb.quartz.profile.DynamicScheduleJobInH2;

@DynamicScheduleJobInH2
public interface QuartzConfigRepository extends CrudRepository<QuartzConfig, Long> {

}
