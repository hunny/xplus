package com.example.bootweb.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.bootweb.websocket.handler.WebSocketWithoutSTOMPSockJsSocketHandler;
import com.example.bootweb.websocket.profile.WebSocketWithoutSTOMPSockJsDemo;

@Configuration
@EnableWebSocket
@WebSocketWithoutSTOMPSockJsDemo
public class WebSocketWithoutSTOMPSockJsConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new WebSocketWithoutSTOMPSockJsSocketHandler(), "/name");
  }

}
