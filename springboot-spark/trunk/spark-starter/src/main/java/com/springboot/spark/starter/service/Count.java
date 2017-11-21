package com.springboot.spark.starter.service;

import com.springboot.spark.starter.profile.SparkWordCount;

@SparkWordCount
public class Count {
  private String word;
  private long count;

  public Count() {
  }

  public Count(String word, long count) {
    this.word = word;
    this.count = count;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }
}
