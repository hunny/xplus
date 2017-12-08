package com.example.bootweb.security.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.example.bootweb.security.handler.IndexLoginAuthenticationSuccessHandler;
import com.example.bootweb.security.profile.IndexAngularJSDemo;

@Configuration
@EnableWebSecurity
@IndexAngularJSDemo
public class IndexAngularJSSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private IndexLoginAuthenticationSuccessHandler successHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        // csrf保护是默认打开的
        .csrf() // CSRF参数，了解更多查阅后续
                // 包括[CSRF](https://springcloud.cc/spring-security-zhcn.html#csrf-include-csrf-token)令牌
                // 和[Cross Site Request Forgery (CSRF)
                // ](https://springcloud.cc/spring-security-zhcn.html#csrf)相关章节
        // .disable(); // 关闭打开的csrf保护

        // A CsrfTokenRepository that persists the CSRF token in a cookie named
        // "XSRF-TOKEN"
        // and reads from the header "X-XSRF-TOKEN" following the conventions of
        // AngularJS.
        // When using with AngularJS be sure to use withHttpOnlyFalse().
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())//
        .and()//
        .authorizeRequests() //
        // requests matched against /css/** and /index are fully
        // accessible，前端静态资源不用验证
        .antMatchers("/**/*.js", "/**/*.css", "/index.css", "/index.js") //
        .permitAll() //
        .antMatchers("/index.html", "/index/login.html") //
        .permitAll() //
        // requests matched against /user/** require a user to be authenticated
        // and must
        // be associated to the USER role，访问以/user/开头的url，需要拥有“USER"角色
        .antMatchers("/index/home.html").hasRole("USER") //
        .and() //
        .formLogin() //
        // form-based authentication is enabled with a custom login page and
        // failure
        // url，
        // 表单认证开启，从"/login"页面登录，登录失败返回“login-error”页面
        // loginPage指定当需要进行认证的时候，重定向的url。
        // 即当需要认证的时候，重定向到/login，让用户输入登录信息进行认证
        // failureUrl指定当认证失败的时候，重定向的url。
        // 即当认证失败的时候，重定向到/login-error，无情告诉用户登录失败
        // loginProcessingUrl指定处理认证请求的url。即前端的登录请求需要传到"login"，才会得到spring
        // security的处理
        .loginProcessingUrl("/login") //
        .usernameParameter("username") //
        .passwordParameter("password") //
        .successHandler(successHandler) //
        .loginPage("/#/login") //
        .failureUrl("/login-error")//
//        .failureHandler(new AuthenticationFailureHandler() {
//          @Override
//          public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//              AuthenticationException exception) throws IOException, ServletException {
//            System.out.println(request.getParameter("username"));
//            System.out.println(request.getParameter("password"));
//          }
//        })
        ; //
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        // 写死的认证，用户名为“user”，且密码为“password”的拥有“USER"身份
        .withUser("user").password("password").roles("USER");
  }
}