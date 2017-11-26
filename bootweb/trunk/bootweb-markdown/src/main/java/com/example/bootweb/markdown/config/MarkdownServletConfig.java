package com.example.bootweb.markdown.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.bootweb.markdown.profile.MarkdownServletProfile;
import com.example.bootweb.markdown.service.MarkdownService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@MarkdownServletProfile
public class MarkdownServletConfig {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
    return objectMapper;
  }

  @Bean
  public ServletRegistrationBean servletRegistrationBean(MarkdownService markdownService) {
    return new ServletRegistrationBean(new MarkdownServlet(markdownService), "*.md");
  }

}
