package com.example.bootweb.jersey.rs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.example.bootweb.jersey.service.Service;

@Component
@Path("/hello")
public class Endpoint {

  private final Service service;

  public Endpoint(Service service) {
    this.service = service;
  }

  @GET
  public String message() {
    return "Hello " + this.service.message();
  }

}
