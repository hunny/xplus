package com.example.bootweb.jersey.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.example.bootweb.jersey.rs.Endpoint;
import com.example.bootweb.jersey.rs.ReverseEndpoint;

@Configuration
@ApplicationPath("/rs")
public class JerseyConfig extends ResourceConfig {

  public JerseyConfig() {
    register(Endpoint.class);
    register(ReverseEndpoint.class);
  }

}
