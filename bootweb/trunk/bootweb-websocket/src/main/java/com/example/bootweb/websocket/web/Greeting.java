package com.example.bootweb.websocket.web;

import com.example.bootweb.websocket.profile.WebSocketDemo;

@WebSocketDemo
public class Greeting {

  private String content;

  public Greeting() {
  }

  public Greeting(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

}
