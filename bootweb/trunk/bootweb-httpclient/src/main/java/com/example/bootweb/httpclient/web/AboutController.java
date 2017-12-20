package com.example.bootweb.httpclient.web;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.httpclient.api.AboutService;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AboutController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private Optional<AboutService> aboutService;
  
  @GetMapping("/about")
  public ResponseEntity<String> getAbout() {

    logger.info("Receive about request.");

    logger.info("AboutService is Present: {}", aboutService.isPresent());
    
    aboutService.ifPresent(service -> {
      logger.info("aboutService.ifPresent: {}", service.about());
    });
    
    return new ResponseEntity<String>("HttpClient get about", HttpStatus.OK);
  }

  @GetMapping("/date/{localDate}")
  public ResponseEntity<String> get(@DateTimeFormat(iso = ISO.DATE) LocalDate localDate) {
    return new ResponseEntity<String>(localDate.toString(), HttpStatus.OK);
  }

  @PostMapping("/login")
  @RequestMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<String> acceptInfo(@RequestParam("username") String username, //
      @RequestParam("password") String password) {

    logger.info("Receive info request {}, {}.", username, password);

    return new ResponseEntity<String>("HttpClient post info", HttpStatus.OK);
  }

}
