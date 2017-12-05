package com.example.bootweb.websocket.web;

import com.example.bootweb.websocket.profile.WebSocketToUserDemo;

/**
 * 消息推送实体
 */
@WebSocketToUserDemo
public class OutMessage {

  private String content;

  public OutMessage() {
  }

  public OutMessage(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

}
