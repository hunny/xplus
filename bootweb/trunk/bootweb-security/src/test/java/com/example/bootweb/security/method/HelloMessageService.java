package com.example.bootweb.security.method;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class HelloMessageService implements MessageService {

  @PreAuthorize("authenticated")
  public String getMessage() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return "Hello " + authentication;
  }

}
