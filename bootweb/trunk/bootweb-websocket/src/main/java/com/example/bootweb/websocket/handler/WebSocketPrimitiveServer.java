package com.example.bootweb.websocket.handler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.bootweb.websocket.profile.WebSocketPrimitiveDemo;

@ServerEndpoint(value = "/websocketServer/{userid}")
@Component
@WebSocketPrimitiveDemo
public class WebSocketPrimitiveServer {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketPrimitiveServer.class);

  private static Map<String, Session> SESSION_POOL = new LinkedHashMap<>();
  private static Map<String, String> SESSION_IDS = new LinkedHashMap<>();
  private Session session;

  @OnOpen // 使用OnOpen注解，在客户端初次连接时触发
  public void onOpen(Session session, @PathParam("userid") String userId) {
    this.session = session;// WebSocket的session，不是httpSession
    SESSION_POOL.put(userId, session);
    SESSION_IDS.put(session.getId(), userId);
    LOGGER.info("当前连接人sessionId为[{}],用户[{}]", this.session.getId(), userId);
  }

  @OnMessage // 使用OnMessage注解，在客户端向服务端发送消息时触发，接收到来自客户端的消息。
  public void onMessage(String message) {
    LOGGER.info("当前发送人sessionId为[{}],发送内容为[{}]", this.session.getId(), message);
  }
  
  @OnClose // 使用该注解，在客户端与服务端断开连接时触发。
  public void onClose() {
    SESSION_POOL.remove(SESSION_IDS.get(session.getId()));
    SESSION_IDS.remove(session.getId());
  }
  
  // 发送消息
  public static void sendMessage(String userId, String message) {
    Session target = SESSION_POOL.get(userId);
    if (null == target) {
      LOGGER.info("当前服务器中无[{}]的session信息，忽略消息发送。", userId);
      return;
    }
    try {
      if (target.isOpen()) {
        target.getBasicRemote().sendText(message);
      } else {
        LOGGER.info("用户[{}]已经离开，忽略消息发送。", userId);
      }
    } catch (IOException e) {
      LOGGER.error("向用户[{}]发送消息失败：[{}]", userId, e.getMessage());
      e.printStackTrace();
    }
  }
  
  // 在线人数
  public static int getOnlineNumber() {
    return SESSION_POOL.size();
  }
  
  // 所有在线用户
  public static Set<String> listOnline() {
    return SESSION_IDS.keySet();
  }
  
  // 向所有客户端发消息
  public static void sendToAll(String message) {
    for (String key : SESSION_IDS.keySet()) {
      sendMessage(SESSION_IDS.get(key), message);
    }
  }

}
