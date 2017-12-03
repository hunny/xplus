package com.example.bootweb.websocket.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.example.bootweb.websocket.profile.WebSocketSessionDemo;

@Component
@WebSocketSessionDemo
public class WebSocketSessionSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Override
  public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
    logger.info("SessionId: {}", headerAccessor.getSessionAttributes().get("sessionId").toString());
  }

}
