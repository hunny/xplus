package com.springboot.spark.starter.web;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/spark", //
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public abstract class SparkController {

  public abstract ResponseEntity<Map<String, String>> about();

}
