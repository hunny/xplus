package com.example.bootweb.websocket.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.example.bootweb.websocket.profile.WebSocketClientDemo;

@WebSocketClientDemo
public class ClientMessageRunner {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final String WEBSOCKET_URI = "";

  public String webSocketClient()
      throws InterruptedException, ExecutionException, TimeoutException {
    logger.info("开始执行。");
    List<Transport> transports = new ArrayList<Transport>(2);
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    transports.add(new RestTemplateXhrTransport());
    SockJsClient sockJsClient = new SockJsClient(transports);
    WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
    stompClient.setMessageConverter(new StringMessageConverter());
    StompSession session = null;
    try {
      session = stompClient.connect(WEBSOCKET_URI, new ClientMessageStompSessionHandler()) //
          .get(1, TimeUnit.SECONDS);
      session.subscribe("/topic" + "/channel", new ClientMessageStompFrameHandler());
      // do your stuff
      // ...
    } finally {
      if (session != null) {
        session.disconnect();
      }
    }
    return null;
  }

  @SuppressWarnings("resource")
  public static void main(String... argv) {
    myRunner();
    new Scanner(System.in).nextLine(); // Don't close immediately.
  }

  private static void myRunner() {
    List<Transport> transports = new ArrayList<>(1);
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    transports.add(new RestTemplateXhrTransport());
    WebSocketClient transport = new SockJsClient(transports);
    WebSocketStompClient stompClient = new WebSocketStompClient(transport);

    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    stompClient.setTaskScheduler(new ConcurrentTaskScheduler());

    String url = "ws://127.0.0.1:8080/my-websocket";
    StompSessionHandler sessionHandler = new ClientMessageStompSessionHandler();
    stompClient.connect(url, sessionHandler);
  }

}
