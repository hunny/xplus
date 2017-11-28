package com.example.bootweb.websocket.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.example.bootweb.websocket.profile.SimpleMessageWebSocketDemo;

@Controller
@SimpleMessageWebSocketDemo
public class SimpleMessageController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @Scheduled(fixedDelay = 1000)
//  @SendTo("/topic/updateService")
  public String send() {
    String message = "Hello, " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    logger.info(message);
    simpMessagingTemplate.convertAndSend("/topic/updateService", message);
    return message;
  }

}
