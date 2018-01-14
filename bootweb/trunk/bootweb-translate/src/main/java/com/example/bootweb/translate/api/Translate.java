package com.example.bootweb.translate.api;

import java.io.Serializable;

public class Translate implements Serializable {

  private static final long serialVersionUID = 4058410163167314579L;
  
  private Class<? extends Lang> from;
  private Class<? extends Lang> to;
  private String text;
  
  public Translate(Class<? extends Lang> from, //
      Class<? extends Lang> to, //
      String text) {
    this.from = from;
    this.to = to;
    this.text = text;
  }

  public Class<? extends Lang> getFrom() {
    return from;
  }

  public void setFrom(Class<? extends Lang> from) {
    this.from = from;
  }

  public Class<? extends Lang> getTo() {
    return to;
  }

  public void setTo(Class<? extends Lang> to) {
    this.to = to;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

}
