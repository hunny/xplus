package com.example.bootweb.websocket.web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.util.concurrent.ListenableFuture;
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

  public static void main(String... argv) throws Exception {
    myRunner();
    // new Scanner(System.in).nextLine(); // Don't close immediately.
  }

  private static void myRunner() throws InterruptedException, ExecutionException, TimeoutException {
    List<Transport> transports = new ArrayList<>(1);
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    transports.add(new RestTemplateXhrTransport());
    WebSocketClient transport = new SockJsClient(transports);
    WebSocketStompClient stompClient = new WebSocketStompClient(transport);

    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    // stompClient.setMessageConverter(new StringMessageConverter());
    stompClient.setTaskScheduler(new ConcurrentTaskScheduler());

    String url = "ws://127.0.0.1:8080/my-websocket";
    CountDownLatch latch = new CountDownLatch(1);
    StompSession session = null;
    try {
      StompSessionHandler sessionHandler = new ClientMessageStompSessionHandler(latch);
      ListenableFuture<StompSession> stompSessions = stompClient.connect(url, sessionHandler);

      session = stompSessions.get(30, TimeUnit.SECONDS);
      latch.await();
    } finally {
      if (session != null) {
        session.disconnect();
      }
    }
  }

}
