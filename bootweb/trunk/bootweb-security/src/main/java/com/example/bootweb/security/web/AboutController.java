package com.example.bootweb.security.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AboutController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @GetMapping("/about")
  public ResponseEntity<Map> getAbout() {
    logger.info("Receive about request.");
    Map<String, String> map = new HashMap<String, String>();
    map.put("data", "Spring Boot & Security & AngularJS demo.");
    return new ResponseEntity<Map>(map, HttpStatus.OK);
  }
  
}
