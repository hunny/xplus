package com.example.bootweb.quartz.web;

import java.util.List;

import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.quartz.dao.JobTriggerBeanMapperDao;
import com.example.bootweb.quartz.entity.JobTriggerBean;
import com.example.bootweb.quartz.profile.SpringBootPersistQuartzMybatis;
import com.example.bootweb.quartz.service.JobScheduleService;

@RestController
@RequestMapping(value = "/quartz", //
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@SpringBootPersistQuartzMybatis
public class JobTriggerBeanController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private JobTriggerBeanMapperDao jobTriggerBeanMapperDao;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private JobScheduleService jobScheduleService;

  @GetMapping(value = "/list")
  public List<JobTriggerBean> list() {
    logger.debug("请求列表。");
    return jobTriggerBeanMapperDao.listJobTrigger();
  }

  @SuppressWarnings("unchecked")
  @PostMapping(value = "/job/add")
  public void addJob(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName,
      @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
    logger.debug("JobClassName: [{}]。", jobClassName);
    Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(jobClassName);
    logger.debug("ClassName: [{}]。", clazz.getName());
    jobScheduleService.add(clazz, jobGroupName, cronExpression);
  }

  @SuppressWarnings("unchecked")
  @PostMapping(value = "/job/pause")
  public void pause(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
    logger.debug("JobClassName: [{}]。", jobClassName);
    Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(jobClassName);
    logger.debug("ClassName: [{}]。", clazz.getName());
    jobScheduleService.pause(clazz, jobGroupName);
  }

  @SuppressWarnings("unchecked")
  @PostMapping(value = "/job/resume")
  public void resume(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
    logger.debug("JobClassName: [{}]。", jobClassName);
    Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(jobClassName);
    logger.debug("ClassName: [{}]。", clazz.getName());
    jobScheduleService.resume(clazz, jobGroupName);
  }

  @SuppressWarnings("unchecked")
  @PostMapping(value = "/job/reschedule")
  public void rescheduleJob(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName,
      @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
    logger.debug("JobClassName: [{}]。", jobClassName);
    Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(jobClassName);
    logger.debug("ClassName: [{}]。", clazz.getName());
    jobScheduleService.reschedule(clazz, jobGroupName, cronExpression);
  }

  @SuppressWarnings("unchecked")
  @DeleteMapping(value = "/job/delete")
  public void deletejob(@RequestParam(value = "jobClassName") String jobClassName,
      @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
    logger.debug("JobClassName: [{}]。", jobClassName);
    Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(jobClassName);
    logger.debug("ClassName: [{}]。", clazz.getName());
    jobScheduleService.delete(clazz, jobGroupName);
  }

  @GetMapping(value = "/list/h2name")
  public List<String> listH2Name() {
    List<String> list = jdbcTemplate.query(//
        "SELECT CATALOG_NAME FROM INFORMATION_SCHEMA.CATALOGS", //
        new SingleColumnRowMapper<String>(String.class));
    return list;
  }

}
