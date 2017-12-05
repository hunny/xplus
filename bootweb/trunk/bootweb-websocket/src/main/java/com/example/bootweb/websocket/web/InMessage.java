package com.example.bootweb.websocket.web;

import com.example.bootweb.websocket.profile.WebSocketToUserDemo;

/**
 * 消息接收实体
 */
@WebSocketToUserDemo
public class InMessage {

  private String name;
  private String id;

  public InMessage() {
  }

  public InMessage(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
