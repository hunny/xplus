package com.example.bootweb.quartz.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.quartz.service.AboutService;

@RestController
public class AboutController {

  @Autowired
  private AboutService aboutService;
  
  @RequestMapping(value = {"/about"}, method = RequestMethod.GET)
  public String about() {
    return aboutService.about();
  }
  
}
