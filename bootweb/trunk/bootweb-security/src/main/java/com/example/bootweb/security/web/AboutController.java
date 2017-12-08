package com.example.bootweb.security.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.security.about.ApplicationAbout;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AboutController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @GetMapping("/about")
  public ResponseEntity<ApplicationAbout> getAbout() {
    logger.info("Receive about request.");
    return new ResponseEntity<ApplicationAbout>(ApplicationAbout.get(getClass()), HttpStatus.OK);
  }

  @GetMapping("/about/{id}")
  public ResponseEntity<Map> getAboutId(@PathVariable("id") Integer id) {
    logger.info("Receive about request id {}.", id);
    Map<String, String> map = new HashMap<String, String>();
    map.put("id", String.valueOf(id));
    return new ResponseEntity<Map>(map, HttpStatus.valueOf(id));
  }

}
