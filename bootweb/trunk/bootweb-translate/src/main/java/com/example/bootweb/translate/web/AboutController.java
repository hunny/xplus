package com.example.bootweb.translate.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.translate.api.CN;
import com.example.bootweb.translate.api.EN;
import com.example.bootweb.translate.api.Translate;
import com.example.bootweb.translate.google.GoogleTranslateBuilder;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AboutController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @GetMapping("/about")
  public ResponseEntity<String> getAbout() {
    
    logger.info("Receive about request.");
    
    return new ResponseEntity<String>("Template Project", HttpStatus.OK);
  }
  
  @GetMapping("/demo/translate/basic")
  public ResponseEntity<String> getTranslateBasic() {
    
    logger.info("Receive about request.");
    
    StringBuffer buffer = new StringBuffer();
    buffer.append("may be included more than once and specifies what to return in the reply.");
    buffer.append("Here are some values for dt. If the value is set, ");
    buffer.append("the following data will be returned:");

    Translate translate = GoogleTranslateBuilder.newBuilder() //
        .source(buffer.toString()) //
        .from(EN.class) //
        .to(CN.class) //
        .build();
    
    return new ResponseEntity<String>(translate.getTarget(), HttpStatus.OK);
  }
  
}
