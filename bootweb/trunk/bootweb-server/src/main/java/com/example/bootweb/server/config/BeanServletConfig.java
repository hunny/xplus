package com.example.bootweb.server.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.bootweb.server.profile.BeanServletProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@BeanServletProfile
public class BeanServletConfig {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
    return objectMapper;
  }
  
  @Bean
  public ServletRegistrationBean servletRegistrationBean(ObjectMapper objectMapper) {
    // ServletName默认值为首字母小写，即myServlet
    return new ServletRegistrationBean(new DemoBeanServlet(objectMapper), "*.md");
  }

}
