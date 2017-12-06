package com.example.bootweb.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.bootweb.security.handler.RedirectAfterLoginAuthenticationSuccessHandler;
import com.example.bootweb.security.profile.RedirectAfterLoginDemo;

@Configuration
@EnableWebSecurity
@RedirectAfterLoginDemo
public class RedirectAfterLoginSpringSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private RedirectAfterLoginAuthenticationSuccessHandler successHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf() //
        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("**/login")) //
        .and() //
        .authorizeRequests() //
        .antMatchers("/user").hasRole("USER") //
        .antMatchers("/admin").hasRole("ADMIN") //
        .and() //
        .formLogin().successHandler(successHandler) //
        .loginPage("/login") //
        .and().logout().permitAll();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
    auth.inMemoryAuthentication().withUser("admin").password("password").roles("ADMIN");
  }

}
