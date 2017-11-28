package com.example.bootweb.websocket.web;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import com.example.bootweb.websocket.profile.WebSocketClientDemo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@WebSocketClientDemo
public class ClientMessageStompSessionHandler implements StompSessionHandler {

  private final Logger log = LoggerFactory.getLogger(getClass());
  
  private ObjectMapper objectMapper;
  
  public ClientMessageStompSessionHandler() {
    objectMapper = objectMapper();
  }
  
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
    return objectMapper;
  }
  
  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    session.subscribe("/topic/clientService", new ClientMessageStompFrameHandler());
//    session.send("/app/hello", "{\"name\":\"Client\"}".getBytes());

    try {
      log.info("New session: {}", session.getSessionId());
      log.info("connectedHeaders: {}", objectMapper.writeValueAsString(connectedHeaders));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
  }

  @Override
  public void handleException(StompSession session, StompCommand command, StompHeaders headers,
      byte[] payload, Throwable exception) {
    exception.printStackTrace();
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return ClientMessage.class;
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    log.info("Received: {}, Type: {}", payload, payload.getClass().getName());
    try {
      log.info("handleFrame headers {}, payload {}", //
          objectMapper.writeValueAsString(headers),
          objectMapper.writeValueAsString(payload));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
  }

  @Override
  public void handleTransportError(StompSession session, Throwable exception) {
    log.info("TransportError: {}", exception.getMessage());
  }

}
