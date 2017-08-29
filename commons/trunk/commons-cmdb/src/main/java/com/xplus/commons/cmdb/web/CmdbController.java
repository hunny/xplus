package com.xplus.commons.cmdb.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stack")
public class CmdbController {

  private final Map<String, String> URLS = new HashMap<String, String>();
  
  @Value("${cmdb.dpos.url:}")
  private String dposUrl;
  
  @Value("${cmdb.mbr.url:}")
  private String mbrUrl;
  
  @Value("${cmdb.prepay.url:}")
  private String prepayUrl;
  
  @PostConstruct
  public void init() {
    URLS.put("dpos", dposUrl);
    URLS.put("mbr", mbrUrl);
    URLS.put("prepay", mbrUrl);
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
