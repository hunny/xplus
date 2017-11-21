package com.springboot.spark.starter.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.spark.starter.profile.SparkWordCount;
import com.springboot.spark.starter.service.Count;
import com.springboot.spark.starter.service.WordCountSparkService;

@RestController
@SparkWordCount
public class SparkWordCountController extends SparkController {

  @Autowired
  private WordCountSparkService wordCountSparkService;

  @RequestMapping("/word/count")
  public ResponseEntity<List<Count>> wordCount() {
    return new ResponseEntity<>(wordCountSparkService.count(), HttpStatus.OK);
  }

  @GetMapping("/about")
  @Override
  public ResponseEntity<Map<String, String>> about() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("application.name", "spark sample.");
    map.put("application.version", "0.0.1-SNAPSHOT");
    map.put("application.active", "SparkWordCount");
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

}
