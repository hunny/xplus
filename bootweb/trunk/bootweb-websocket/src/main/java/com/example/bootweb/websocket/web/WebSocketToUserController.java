package com.example.bootweb.websocket.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.bootweb.websocket.handler.SocketSessionRegistry;

/**
 * 聊天控制器
 */
@Controller
public class WebSocketToUserController {

  /** session操作类 */
  @Autowired
  private SocketSessionRegistry webAgentSessionRegistry;

  /** 消息发送工具 */
  @Autowired
  private SimpMessagingTemplate messageTemplate;

  /**
   * 用户广播 发送消息广播 用于内部发送使用
   * 
   */
  @GetMapping(value = "/msg/sendtoall")
  public @ResponseBody OutMessage SendToCommUserMessage(HttpServletRequest request) {
    String msg = request.getParameter("msg");
    List<String> keys = webAgentSessionRegistry.getAllSessionIds().entrySet().stream()
        .map(Map.Entry::getKey).collect(Collectors.toList());
    keys.forEach(x -> {
      String sessionId = webAgentSessionRegistry.getSessionIds(x).stream().findFirst().get()
          .toString();
      messageTemplate.convertAndSendToUser(sessionId, "/topic/message",
          new OutMessage("AllSend, [" + msg + "] At:" + //
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())),
          createHeaders(sessionId));

    });
    return new OutMessage("sendtouser, [" + msg + "]");
  }

  /**
   * 同样的发送消息 只不过是ws版本 http请求不能访问 根据用户key发送消息
   */
  @MessageMapping("/msg/tosingleuser")
  public void greeting2(InMessage message) throws Exception {
    // 这里没做校验
    String sessionId = webAgentSessionRegistry.getSessionIds(message.getId()).stream().findFirst()
        .get();
    messageTemplate.convertAndSendToUser(sessionId, "/topic/message",
        new OutMessage("single send to：" + message.getId() + ", from:" + message.getName() + "!"),
        createHeaders(sessionId));
  }

  @SuppressWarnings("static-method")
  private MessageHeaders createHeaders(String sessionId) {
    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
        .create(SimpMessageType.MESSAGE);
    headerAccessor.setSessionId(sessionId);
    headerAccessor.setLeaveMutable(true);
    return headerAccessor.getMessageHeaders();
  }

}
