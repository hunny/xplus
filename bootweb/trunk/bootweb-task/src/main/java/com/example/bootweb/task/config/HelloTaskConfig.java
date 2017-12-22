package com.example.bootweb.task.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.bootweb.task.profile.HelloTaskProfile;

@Configuration
@EnableTask
@HelloTaskProfile
public class HelloTaskConfig {

  private final Logger logger = LoggerFactory.getLogger(HelloTaskConfig.class);

  @Bean
  public CommandLineRunner commandLineRunner() {
    return strings -> {
      logger.info("Hello, Task! Run my command line.");
      System.out.println("Hello, Task! Executed at :" + new SimpleDateFormat().format(new Date()));
    };
  }

}
