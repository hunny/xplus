package com.springboot.spark.starter.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.spark.starter.profile.SparkWordCount;
import com.springboot.spark.starter.service.Count;
import com.springboot.spark.starter.service.WordCountSparkService;

@RequestMapping("/spark")
@Controller
@SparkWordCount
public class SparkController {

  @Autowired
  private WordCountSparkService wordCountSparkService;

  @RequestMapping("/word/count")
  public ResponseEntity<List<Count>> wordCount() {
      return new ResponseEntity<>(wordCountSparkService.count(), HttpStatus.OK);
  }
  
}
