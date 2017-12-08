package com.example.bootweb.security.conditional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMyProperty(name = "my.name")
public class ConditionUsingClass {

  @Bean
  public HelloWorldService helloWorldService() {
    return new HelloWorldService();
  }
  
}
