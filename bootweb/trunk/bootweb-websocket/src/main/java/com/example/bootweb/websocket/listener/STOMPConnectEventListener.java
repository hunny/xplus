package com.example.bootweb.websocket.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.example.bootweb.websocket.handler.SocketSessionRegistry;
import com.example.bootweb.websocket.profile.WebSocketToUserDemo;

/**
 * STOMP监听类 用于session注册 以及key值获取
 */
@WebSocketToUserDemo
public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {

  @Autowired
  private SocketSessionRegistry webAgentSessionRegistry;

  @Override
  public void onApplicationEvent(SessionConnectEvent event) {
    StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
    // login get from browser
    String agentId = stompHeaderAccessor.getNativeHeader("login").get(0);
    String sessionId = stompHeaderAccessor.getSessionId();
    webAgentSessionRegistry.registerSessionId(agentId, sessionId);
  }

}
