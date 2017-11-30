package com.example.bootweb.websocket.handler;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.bootweb.websocket.profile.WebSocketWithoutSTOMPSockJsDemo;

@Component
@WebSocketWithoutSTOMPSockJsDemo
public class WebSocketWithoutSTOMPSockJsSocketHandler extends TextWebSocketHandler {
  
  private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
  
  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message)
      throws InterruptedException, IOException {
    String str = message.getPayload();
    for(WebSocketSession webSocketSession : sessions) {
      if (webSocketSession.isOpen()) {
        System.out.println(str);
        webSocketSession.sendMessage(new TextMessage("Hello " + str + " !"));
      }
    }
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    //the messages will be broadcasted to all users.
    sessions.add(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    super.afterConnectionClosed(session, status);
    sessions.remove(session);
  }
  
}
