package com.example.bootweb.security.method;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
  
  @Bean
  public MessageService messageService() {
    return new HelloMessageService();
  }

}
