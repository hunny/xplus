package com.example.bootweb.translate.api;

import java.io.Serializable;

public class Params implements Serializable {

  private static final long serialVersionUID = 2944857758964413899L;

  private String key;
  private String value;

  public Params(String key, String value) {
    super();
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Params [key=");
    builder.append(key);
    builder.append(", value=");
    builder.append(value);
    builder.append("]");
    return builder.toString();
  }

}
