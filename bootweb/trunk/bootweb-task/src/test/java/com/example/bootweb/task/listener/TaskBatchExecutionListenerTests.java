package com.example.bootweb.task.listener;

import org.junit.After;
import org.junit.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

public class TaskBatchExecutionListenerTests {

  public static final String[] ARGS = new String[] {
      "--spring.cloud.task.closecontext_enable=false" };

  private ConfigurableApplicationContext applicationContext;

  @After
  public void tearDown() {
    if (this.applicationContext != null) {
      this.applicationContext.close();
    }
  }

  @Test
  public void testAutobuiltDataSource() {
    this.applicationContext = SpringApplication.run(new Class[] {
        BaseApplicationConfig.class }, ARGS);
  }
  
  public static class BaseApplicationConfig {
    
    @Bean
    public CommandLineRunner commandLineRunner() {
      return strings -> {
        System.err.println("OK");
      };
    }
    
  }

}
