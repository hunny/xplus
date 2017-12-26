package com.example.springboot.actuator.runner;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

@Component
public class SpringFactoriesRunner implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(SpringFactoriesRunner.class);
  
  @Override
  public void run(String... args) throws Exception {
    List<String> list = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, null);
    list.forEach(str -> {
      logger.info("SpringFactories信息：{}", str);
    });
  }

}
