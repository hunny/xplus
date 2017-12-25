package com.example.springboot.actuator.config;

import java.util.Collections;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class ActuatorConfig {


  @Bean
  public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    return new InMemoryUserDetailsManager(Collections.singleton(//
        User.withUsername("user")//
            .password("password")//
            .roles("USER")//
//            .roles("USER", "ACTUATOR")//
            .build()//
    ));
  }

  @Bean
  public HealthIndicator healthIndicator() {
    return new HealthIndicator() {

      @Override
      public Health health() {
        return Health.status(Status.UP).withDetail("hello", "world").build();
      }

    };
  }
  
}
