package com.example.springboot.activemq;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
public class QueueConfig {

  @Bean
  public Queue queue() {
    return new ActiveMQQueue("sample.queue");
  }

}
