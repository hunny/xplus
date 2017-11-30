package com.example.bootweb.security.web;

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
  public ResponseEntity<String> getAbout() {
    
    logger.info("Receive about request.");
    
    return new ResponseEntity<String>("Security Project", HttpStatus.OK);
  }
  
}
