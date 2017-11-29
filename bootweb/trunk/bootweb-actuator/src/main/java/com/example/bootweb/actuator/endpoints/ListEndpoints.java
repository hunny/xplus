package com.example.bootweb.actuator.endpoints;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.stereotype.Component;

@Component
public class ListEndpoints extends AbstractEndpoint<List<Endpoint>> {

  private List<Endpoint> endpoints;

  @Autowired
  public ListEndpoints(List<Endpoint> endpoints) {
    super("listendpoints");
    this.endpoints = endpoints;
  }

  @Override
  public List<Endpoint> invoke() {
    return endpoints;
  }

}
