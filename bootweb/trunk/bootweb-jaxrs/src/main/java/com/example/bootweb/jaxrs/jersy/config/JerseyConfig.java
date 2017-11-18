package com.example.bootweb.jaxrs.jersy.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ApplicationPath("/api")
@Profile("JerseyConfig")
public class JerseyConfig extends ResourceConfig {

  public JerseyConfig() {
    //配置restful package.
    packages("com.example.bootweb.jaxrs.api");
  }
  
}
