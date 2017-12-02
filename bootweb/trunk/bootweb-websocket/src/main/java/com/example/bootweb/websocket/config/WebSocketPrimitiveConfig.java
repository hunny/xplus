package com.example.bootweb.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.example.bootweb.websocket.profile.WebSocketPrimitiveDemo;

@Configuration
@WebSocketPrimitiveDemo
public class WebSocketPrimitiveConfig {
  
  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }

}
