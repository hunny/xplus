package com.example.bootweb.jaxrs.jersy.configbean;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Profile;

@Profile("JerseyConfigServlet")
public class JerseyConfigServlet extends ResourceConfig {

  public JerseyConfigServlet() {
    // 配置restful package.
    packages("com.example.bootweb.jaxrs.api");
  }
  
}
