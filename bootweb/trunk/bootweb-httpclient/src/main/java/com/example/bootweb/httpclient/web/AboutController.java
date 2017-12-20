package com.example.bootweb.httpclient.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public class AboutController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @GetMapping("/about")
  public ResponseEntity<String> getAbout() {

    logger.info("Receive about request.");

    return new ResponseEntity<String>("HttpClient get about", HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<String> acceptInfo(@RequestParam("username") String username, //
      @RequestParam("password") String password) {

    logger.info("Receive info request {}, {}.", username, password);

    return new ResponseEntity<String>("HttpClient post info", HttpStatus.OK);
  }

}
