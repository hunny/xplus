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
    permitAll(http);
  }

  /**
   * 允许所有可访问
   * 
   * @param http
   * @throws Exception
   */
  protected void permitAll(HttpSecurity http) throws Exception {
    http //
        .authorizeRequests() //
        .anyRequest() //
        .permitAll() //
        .and() // java配置使用and()方法相当于XML标签的关闭。 这样允许我们继续配置父类节点。
        .logout() //
        .permitAll();
  }

  /**
   * 认证授权访问
   * 
   * @param http
   * @throws Exception
   */
  protected void authenticate(HttpSecurity http) throws Exception {
    http //
        .authorizeRequests() //
        .antMatchers("/", "/home", "/index.html", "/index/*").permitAll() //
        .anyRequest() // 确保我们应用中的所有请求都需要用户被认证
        .authenticated() //
        .and() // java配置使用and()方法相当于XML标签的关闭。 这样允许我们继续配置父类节点。
        .formLogin() // 允许用户进行基于表单的认证
        .loginPage("/login") // 指定登录页的路径，一个POST请求到/login用来验证用户：登录名参数必须被命名为username,密码参数必须被命名为password
        .permitAll() // 我们必须允许所有用户访问我们的登录页（例如为验证的用户），这个formLogin().permitAll()方法允许基于表单登录的所有的URL的所有用户的访问。
        .and() // java配置使用and()方法相当于XML标签的关闭。 这样允许我们继续配置父类节点。
        .logout().permitAll(); //
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
  }
}
