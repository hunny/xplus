package com.example.bootweb.angularjs.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  @RequestMapping("about")
  public String about() {
    return "Hello World!";
  }
  
}
