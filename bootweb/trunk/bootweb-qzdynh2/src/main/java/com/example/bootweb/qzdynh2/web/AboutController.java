package com.example.bootweb.qzdynh2.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.qzdynh2.service.AboutService;

@RestController
public class AboutController {

  @Autowired
  private AboutService aboutService;
  
  @RequestMapping(value = {"/about"}, method = RequestMethod.GET)
  public String about() {
    return aboutService.about();
  }
  
}
