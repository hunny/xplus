package com.example.bootweb.accessory.druid;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
@ConditionDruidServlet
public class DruidServletConfig {

  @Bean
  public Log4jFilter log4jFilter() {
    Log4jFilter log4jFilter = new Log4jFilter();
    log4jFilter.setStatementExecutableSqlLogEnable(true);
    log4jFilter.setStatementCreateAfterLogEnabled(false);
    return log4jFilter;
  }

  @Bean
  public ServletRegistrationBean registrationBean(DruidServletValue druidServletValue) {
    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
        new StatViewServlet()); // 添加初始化参数：initParams
    // Using druid/index.html to access.
    servletRegistrationBean.addUrlMappings( //
        druidServletValue.getUrlMappings()); // "/druid/*"
    // 白名单：
    servletRegistrationBean.addInitParameter("allow", //
        druidServletValue.getAllow()); // "127.0.0.1,172.17.2.172"
    // IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to
    // view this page.
    servletRegistrationBean.addInitParameter("deny", //
        druidServletValue.getDeny());// "172.17.2.173"
    // 登录查看信息的账号密码.
    servletRegistrationBean.addInitParameter("loginUsername", //
        druidServletValue.getUsername());
    servletRegistrationBean.addInitParameter("loginPassword", //
        druidServletValue.getPassword());
    // 是否能够重置数据.
    servletRegistrationBean.addInitParameter("resetEnable", //
        druidServletValue.getResetEnable());// "false"
    return servletRegistrationBean;
  }

  @Bean
  public FilterRegistrationBean filterRegistrationBean(DruidServletValue druidServletValue) {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
    // 添加过滤规则.
    filterRegistrationBean.addUrlPatterns("/*");
    // 添加不需要忽略的格式信息.
    filterRegistrationBean.addInitParameter("exclusions", //
        druidServletValue.getExclusions());// "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"
    return filterRegistrationBean;
  }

}
