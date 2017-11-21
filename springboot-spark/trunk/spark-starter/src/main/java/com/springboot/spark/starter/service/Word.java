package com.springboot.spark.starter.service;

import com.springboot.spark.starter.profile.SparkWordCount;

@SparkWordCount
public class Word {
  
  private String word;

  public Word() {
  }

  public Word(String word) {
    this.word = word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getWord() {
    return word;
  }
}
