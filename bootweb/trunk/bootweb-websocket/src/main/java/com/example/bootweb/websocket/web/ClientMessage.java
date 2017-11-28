package com.example.bootweb.websocket.web;

import java.io.Serializable;

import com.example.bootweb.websocket.profile.WebSocketClientDemo;

@WebSocketClientDemo
public class ClientMessage implements Serializable {

  private static final long serialVersionUID = -2092434647828354977L;

  public ClientMessage() {
    super();
  }

  public ClientMessage(String message) {
    super();
    this.message = message;
  }

  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "ClientMessage [message=" + message + "]";
  }

}
