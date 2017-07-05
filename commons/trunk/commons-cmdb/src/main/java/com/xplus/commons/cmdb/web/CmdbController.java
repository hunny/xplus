package com.xplus.commons.cmdb.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stack")
public class CmdbController {

  @RequestMapping(value = "/get/{app}/{requestid}", method = RequestMethod.GET)
  public Stack get(@PathVariable String app, //
      @PathVariable String requestid) {
    Stack stack = new Stack();
    stack.setServerUrl("http://localhost:8080/test/");
    return stack;
  }

  @RequestMapping(value = "/about", method = RequestMethod.GET)
  public String about() {
    return "about";
  }

}
