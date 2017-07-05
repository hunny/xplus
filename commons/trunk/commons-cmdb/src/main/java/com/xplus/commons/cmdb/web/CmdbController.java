package com.xplus.commons.cmdb.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stack")
public class CmdbController {

  private static final Map<String, String> URLS = new HashMap<String, String>();
  
  static {
    URLS.put("dpos", "http://localhost:8082/rs/");
    URLS.put("mbr", "http://localhost:8080/web/");
    URLS.put("prepay", "http://localhost:8080/rs/");
  }
  
  @RequestMapping(value = "/get/{app}/{requestid}", method = RequestMethod.GET)
  public Stack get(@PathVariable String app, //
      @PathVariable String requestid) {
    Stack stack = new Stack();
    stack.setServerUrl(URLS.get(app));
    return stack;
  }

  @RequestMapping(value = "/about", method = RequestMethod.GET)
  public String about() {
    return "about";
  }

}
