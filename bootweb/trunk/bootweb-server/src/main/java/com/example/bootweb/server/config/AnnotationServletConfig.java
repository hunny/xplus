package com.example.bootweb.server.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import com.example.bootweb.server.profile.AnnotationServletProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@ServletComponentScan
@AnnotationServletProfile
public class AnnotationServletConfig {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
    return objectMapper;
  }
  
  /**
   * 修改DispatcherServlet默认配置
   */
  @Bean
  public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
      ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
      registration.getUrlMappings().clear();
      registration.addUrlMappings("*.do");
      registration.addUrlMappings("*.json");
      return registration;
  }

}
