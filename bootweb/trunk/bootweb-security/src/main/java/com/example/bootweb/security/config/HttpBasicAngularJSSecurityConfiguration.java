package com.example.bootweb.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.example.bootweb.security.profile.HttpBasicAngularJSDemo;

@Configuration
@HttpBasicAngularJSDemo
public class HttpBasicAngularJSSecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic() //
        .and() //
        .authorizeRequests() //
        .antMatchers("/index-HttpBasicAngularJS.html", "*.css", "*.js") //
        .permitAll() //
        .anyRequest() //
        .authenticated() //
        .and() //
        .csrf() //
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) //
        .and() //
        .logout() //
        .logoutSuccessUrl("/");
  }
}
