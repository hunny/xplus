package com.springboot.spark.starter.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.spark.starter.profile.SparkPiEstimation;
import com.springboot.spark.starter.service.PiEstimationSparkService;

@RestController
@SparkPiEstimation
public class SparkPiEstimationController extends SparkController {

  @Autowired
  private PiEstimationSparkService piEstimationService;

  @RequestMapping("/pi/estimat/{samples}")
  public ResponseEntity<Double> estimat(@PathVariable("samples") int samples) {
    return new ResponseEntity<>(piEstimationService.estimate(samples), HttpStatus.OK);
  }
  
  @RequestMapping("/pi/map/reduce/{slices}")
  public ResponseEntity<Double> mapReduce(@PathVariable("slices") int slices) {
    return new ResponseEntity<>(piEstimationService.mapReduce(slices), HttpStatus.OK);
  }

  @GetMapping("/about")
  @Override
  public ResponseEntity<Map<String, String>> about() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("application.name", "spark sample.");
    map.put("application.version", "0.0.1-SNAPSHOT");
    map.put("application.active", "SparkPiEstimation");
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

}
