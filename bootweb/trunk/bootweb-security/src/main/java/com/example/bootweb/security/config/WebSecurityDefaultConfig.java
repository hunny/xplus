package com.example.bootweb.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.example.bootweb.security.profile.OnNoProfileCondition;

/**
 * 中文参考手册{@link https://springcloud.cc/spring-security-zhcn.html}
 */
@Configuration
@EnableWebSecurity
@OnNoProfileCondition
public class WebSecurityDefaultConfig extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
//    http //
//        .authorizeRequests() //
//        .antMatchers("/", "/home", "/index.html", "/index/*").permitAll() //
//        .anyRequest().authenticated() //
//        .and() //
//        .formLogin() //
//        .loginPage("/login") //
//        .permitAll() //
//        .and() //
//        .logout().permitAll(); //
    http.authorizeRequests().anyRequest().permitAll();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
  }
}
