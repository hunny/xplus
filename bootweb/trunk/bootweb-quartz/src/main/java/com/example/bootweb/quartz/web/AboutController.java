package com.example.bootweb.quartz.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.quartz.service.AboutService;

@RestController
public class AboutController {

  @Autowired
  private AboutService aboutService;

  @Autowired
  private Environment environment;

  @RequestMapping(value = { "/about" }, method = RequestMethod.GET)
  public String about() {
    return aboutService.about();
  }

  /**
   * 参考[集成maven和Spring
   * boot的profile功能]{@link http://blog.csdn.net/lihe2008125/article/details/50443491}
   * 
   * @return list
   */
  @RequestMapping(value = { "/active/profiles" }, method = RequestMethod.GET)
  public List<String> profile() {
    return Arrays.asList(environment.getActiveProfiles());
  }

}
