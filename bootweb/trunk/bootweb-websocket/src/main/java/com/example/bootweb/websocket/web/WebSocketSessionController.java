package com.example.bootweb.websocket.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.websocket.profile.WebSocketSessionDemo;
import com.google.gson.Gson;

@RestController
@WebSocketSessionDemo
public class WebSocketSessionController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private SimpMessageSendingOperations messagingTemplate;//注入SimpMessageSendingOperations可以在任何时间发送消息
  
  @SuppressWarnings("unchecked")
  @MessageMapping("/message")
  public void processMessageFromClient(@Payload String message, //
      SimpMessageHeaderAccessor headerAccessor) {
    String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
    logger.info("请求的sessionId={}, message={}", sessionId, message);
    headerAccessor.setSessionId(sessionId);
    Map<String, Object> msg = new Gson().fromJson(message, Map.class);
    messagingTemplate.convertAndSend("/topic/reply", msg.get("name"));
  }
  
}
