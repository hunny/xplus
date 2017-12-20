package com.example.bootweb.accessory.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.accessory.api.Httpable;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AboutController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private Httpable<String> httpable;
  
  @GetMapping("/about")
  public ResponseEntity<String> getAbout() {
    
    logger.info("Receive about request.");
    
    return new ResponseEntity<String>("Template Project", HttpStatus.OK);
  }
  
  @GetMapping("/tianyancha/list")
  public ResponseEntity<String> list() {
//    String url = "https://www.baidu.com";
//    String url = "http://localhost:8081/about";
    String url = "https://m.tianyancha.com/";
    String text = httpable.get(url);
    return new ResponseEntity<String>(text, HttpStatus.OK);
  }
  
}
