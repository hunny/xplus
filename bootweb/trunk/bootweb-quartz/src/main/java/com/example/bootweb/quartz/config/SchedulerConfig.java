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
