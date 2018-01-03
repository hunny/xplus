package com.example.springboot.actuator.security.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Bean
  public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    return new InMemoryUserDetailsManager(Arrays.asList(//
        User.withUsername("user") //
            .password("password") //
            .authorities("ROLE_USER") //
            .build(), //
        User.withUsername("admin") //
            .password("admin") //
            .authorities("ROLE_ACTUATOR", "ROLE_USER") //
            .build() //
    ));
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests() //
        .antMatchers("/status", "/info")
        .permitAll() //
        .antMatchers("/*") //
        .hasRole("ACTUATOR") //
        .antMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/**/favicon.ico") //
        .permitAll() //
        .antMatchers("/foo") //
        .permitAll() //
        .antMatchers("/**") //
        .hasRole("USER") //
        .and() //
        .cors() //
        .and() //
        .httpBasic();
  }

}
