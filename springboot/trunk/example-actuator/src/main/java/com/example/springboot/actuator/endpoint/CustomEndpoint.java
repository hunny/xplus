package com.example.springboot.actuator.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.stereotype.Component;

@Component
public class CustomEndpoint implements Endpoint<List<String>> {

  public String getId() {
    return "customEndpoint";
  }

  public boolean isEnabled() {
    return true;
  }

  public boolean isSensitive() {
    return true;
  }

  public List<String> invoke() {
    // Custom logic to build the output
    List<String> messages = new ArrayList<String>();
    messages.add("This is message 1");
    messages.add("This is message 2");
    return messages;
  }
}
