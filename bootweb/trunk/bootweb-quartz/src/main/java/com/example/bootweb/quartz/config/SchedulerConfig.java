package com.example.bootweb.quartz.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.example.bootweb.quartz.profile.SpringBootPersistQuartz;

/**
 * Quartz任务调度框架之最全Quartz系统参数配置详解{@link http://blog.csdn.net/zixiao217/article/details/53091812}
 */
@Configuration
@SpringBootPersistQuartz
public class SchedulerConfig {

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource primaryDataSource() {
      return DataSourceBuilder.create().build();
  }

  /**
   * [Spring Boot配置多个DataSource](https://www.liaoxuefeng.com/article/001484212576147b1f07dc0ab9147a1a97662a0bd270c20000)
   * @return
   */
//  @Bean(name = "quartzDatasource")
//  @ConfigurationProperties(prefix = "org.quartz.dataSource.qzDS")
//  public DataSource secondDataSource() {
//      return DataSourceBuilder.create().build();
//  }
  
  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) throws IOException {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();
    // 用于quartz集群, QuartzScheduler 启动时更新己存在的Job
    factory.setOverwriteExistingJobs(true);
    // 延时启动，应用启动1秒后
    factory.setStartupDelay(1);
    factory.setQuartzProperties(quartzProperties());
    factory.setDataSource(dataSource);
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
   * 
   * @return QuartzInitializerListener
   */
  @Bean
  public QuartzInitializerListener executorListener() {
    return new QuartzInitializerListener();
  }

}
