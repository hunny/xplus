package com.example.bootweb.map.web;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.map.service.GPSService;

@RestController
@RequestMapping(value = "/gps", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GPSController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private GPSService gPSService;
  
  @GetMapping("/bd09/{lon}/{lat}/to/gcj02")
  public GPS getGCJ02(@PathVariable("lon") double lon, 
      @PathVariable("lat") double lat) {
    
    logger.info("lon '{}', lat '{}'", lon, lat);
    
    GPS bd09 = new GPS();
    bd09.setLongitude(BigDecimal.valueOf(lon));
    bd09.setLatitude(BigDecimal.valueOf(lat));
    
    return gPSService.BD09toGCJ02(bd09);
  }
  
  @GetMapping("/gcj02/{lon}/{lat}/to/bd09")
  public GPS getBD09(@PathVariable("lon") double lon, 
      @PathVariable("lat") double lat) {
    
    logger.info("lon '{}', lat '{}'", lon, lat);
    
    GPS gcj02 = new GPS();
    gcj02.setLongitude(BigDecimal.valueOf(lon));
    gcj02.setLatitude(BigDecimal.valueOf(lat));
    
    return gPSService.GCJ02toBD09(gcj02);
  }
  
}
