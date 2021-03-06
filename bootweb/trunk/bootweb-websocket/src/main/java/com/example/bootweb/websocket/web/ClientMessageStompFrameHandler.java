package com.example.bootweb.websocket.web;

import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import com.example.bootweb.websocket.profile.WebSocketClientDemo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@WebSocketClientDemo
public class ClientMessageStompFrameHandler implements StompFrameHandler {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  private ObjectMapper objectMapper;
  private CountDownLatch latch;
  private int num = 0;
  
  public ClientMessageStompFrameHandler(CountDownLatch latch) {
    objectMapper = objectMapper();
    this.latch = latch;
  }
  
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
    return objectMapper;
  }
  
  @Override
  public Type getPayloadType(StompHeaders headers) {
    try {
      logger.info("getPayloadType, {}", objectMapper.writeValueAsString(headers));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return ClientMessage.class;
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    try {
      logger.info("{}. handleFrame headers {}, payload {}", //
          num, //
          objectMapper.writeValueAsString(headers), //
          objectMapper.writeValueAsString(payload));
      num ++;
      if (num > 10) {
        this.latch.countDown();
      };
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
  }

}
