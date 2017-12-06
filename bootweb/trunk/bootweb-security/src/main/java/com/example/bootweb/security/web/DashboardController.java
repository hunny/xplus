package com.example.bootweb.security.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.security.profile.RedirectAfterLoginDemo;

@RestController
@RedirectAfterLoginDemo
public class DashboardController {

  @RequestMapping(value = "/admin", method = RequestMethod.GET)
  public ResponseEntity<String> admin() {
    return new ResponseEntity<String>("admin", HttpStatus.OK);
  }

  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public ResponseEntity<String> user() {
    return new ResponseEntity<String>("user", HttpStatus.OK);
  }

}
