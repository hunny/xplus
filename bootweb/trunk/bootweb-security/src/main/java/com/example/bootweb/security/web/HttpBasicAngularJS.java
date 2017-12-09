package com.example.bootweb.security.web;

import java.util.UUID;

import com.example.bootweb.security.profile.HttpBasicAngularJSDemo;

@HttpBasicAngularJSDemo
public class HttpBasicAngularJS {

  private String id;
  private String msg;

  public HttpBasicAngularJS(String msg) {
    this.id = UUID.randomUUID().toString();
    this.msg = msg;
  }

  public String getId() {
    return id;
  }

  public String getMsg() {
    return msg;
  }

  @Override
  public String toString() {
    return "Greeting [msg=" + msg + "]";
  }
}
