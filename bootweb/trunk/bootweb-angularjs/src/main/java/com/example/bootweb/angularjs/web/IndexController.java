package com.example.bootweb.angularjs.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  @RequestMapping(value = "about", method = RequestMethod.GET)
  public String about() {
    return "Hello World!";
  }
  
}
