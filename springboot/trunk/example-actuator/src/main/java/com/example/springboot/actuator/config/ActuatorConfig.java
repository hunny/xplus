package com.example.springboot.actuator.config;

import java.util.Arrays;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class ActuatorConfig {

  @Bean
  public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    UserDetails user = User.withUsername("user")//
        .password("password")//
        .roles("USER")//
        .build();//
    UserDetails actuator = User.withUsername("actuator")//
        .password("password")//
        .roles("USER", "ACTUATOR")//
        .build();//
    return new InMemoryUserDetailsManager(Arrays.asList(user, actuator));
  }

  // @Bean
  // public HealthIndicator healthIndicator() {
  // return new MyHealthIndicator();
  // }

}
