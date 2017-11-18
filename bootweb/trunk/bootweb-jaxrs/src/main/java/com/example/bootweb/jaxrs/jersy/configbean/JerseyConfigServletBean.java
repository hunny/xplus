package com.example.bootweb.jaxrs.jersy.configbean;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("JerseyConfigServlet")
public class JerseyConfigServletBean {

  @Bean
  public ServletRegistrationBean jerseyServlet() {
    ServletRegistrationBean registration = new ServletRegistrationBean( //
        new ServletContainer(), "/api-servlet/*");
    // our rest resources will be available in the path /api-servlet/*
    registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, //
        JerseyConfigServlet.class.getName());
    return registration;
  }

}
