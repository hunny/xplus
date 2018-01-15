package com.example.bootweb.translate.api;

import java.io.Serializable;

public class Translate implements Serializable {

  private static final long serialVersionUID = 4058410163167314579L;

  private Class<? extends Lang> from;
  private Class<? extends Lang> to;
  private String text;
  private String target;

  public Translate() {
    this(null);
  }
  
  public Translate(String text) {
    this(EN.class, CN.class, text);
  }
  
  public Translate(Class<? extends Lang> from, //
      Class<? extends Lang> to, //
      String text) {
    this.from = from;
    this.to = to;
    this.text = text;
  }

  /** 来源语言 */
  public Class<? extends Lang> getFrom() {
    return from;
  }

  public void setFrom(Class<? extends Lang> from) {
    this.from = from;
  }

  /** 目标语言 */
  public Class<? extends Lang> getTo() {
    return to;
  }

  public void setTo(Class<? extends Lang> to) {
    this.to = to;
  }

  /** 来源语言文字 */
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  /** 目标语言文字 */
  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

}
