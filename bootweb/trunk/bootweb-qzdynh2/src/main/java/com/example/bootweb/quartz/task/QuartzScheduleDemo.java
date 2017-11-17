package com.example.bootweb.quartz.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.bootweb.quartz.profile.DynamicScheduleJobInH2;

@Component
@DynamicScheduleJobInH2
public class QuartzScheduleDemo {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Scheduled(cron = "0 0/1 * * * ?") // 每分钟执行一次
  public void cron() throws Exception {
    logger.info("@Scheduled cron '{}', 时间'{}'", "0 0/1 * * * ?", new Date());
  }

  @Scheduled(fixedRate = 5000) // 每5秒执行一次
  public void fixedRate() throws Exception {
    logger.info("@Scheduled fixedRate '{}', 时间'{}'", 5000, new Date());
  }

}
