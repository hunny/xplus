# Spring Boot Session STOMP WebSocket

Sometimes, we want to track the session of the same browser.In this post, we will be discussing about how to maintain spring session during websocket connection through HandshakeInterceptor.Doing so we can track user session during every websocket request and can utilize this session to track client activities from the server even after the server is connected through websocket protocol.Now let us look into the code how can we maintain spring session during websocket connection using Spring boot.

## Background

As we know, before making a websocket connection to a server, client makes a handshake request as an upgrade request and this request is a HTTP request.Hence, to maintain a websocket session, we require to intercept this HTTP request and keep the session id somewhere from where it can be accessed everytime whenever a websocket request is made.Here we will be using STOMP header attributes to track the session.  

## Server Side

### Add requirement dependency

```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>
```

### Add HandShakeInterceptor

Now let us define the httpInterceptor.We have overriden beforeHandshake() to intercept the handshake request and set our custom attributes.We are setting our custom attribute sessionId with actual sessionId and this is the same key which we will be using later to track the session from our controller class.

Interceptor for WebSocket handshake requests. Can be used to inspect the handshake request and response as well as to pass attributes to the target {@link WebSocketHandler}.

```java
package com.example.bootweb.websocket.handler;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.example.bootweb.websocket.profile.WebSocketSessionDemo;

/**
 * Interceptor for WebSocket handshake requests. Can be used to inspect the
 * handshake request and response as well as to pass attributes to the target
 * {@link WebSocketHandler}.
 * 
 */
@WebSocketSessionDemo
public class WebSocketSessionHandShakeInterceptor implements HandshakeInterceptor {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * Invoked before the handshake is processed.
   * 
   * @param request
   *          the current request
   * @param response
   *          the current response
   * @param wsHandler
   *          the target WebSocket handler
   * @param attributes
   *          attributes from the HTTP handshake to associate with the WebSocket
   *          session; the provided attributes are copied, the original map is not
   *          used.
   * @return whether to proceed with the handshake ({@code true}) or abort
   *         ({@code false})
   */
  @Override
  public boolean beforeHandshake(ServerHttpRequest request, //
      ServerHttpResponse response, //
      WebSocketHandler handler, //
      Map<String, Object> attributes) throws Exception {
    if (request instanceof ServletServerHttpRequest) {
      ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
      HttpSession session = servletRequest.getServletRequest().getSession();
      logger.info("接收到请求, sessionId={}, attributes={}。", session.getId(), attributes);
      attributes.put("sessionId", session.getId());
    }
    return true;
  }

  /**
   * Invoked after the handshake is done. The response status and headers indicate
   * the results of the handshake, i.e. whether it was successful or not.
   * 
   * @param request
   *          the current request
   * @param response
   *          the current response
   * @param wsHandler
   *          the target WebSocket handler
   * @param exception
   *          an exception raised during the handshake, or {@code null} if none
   */
  @Override
  public void afterHandshake(ServerHttpRequest request, //
      ServerHttpResponse response, //
      WebSocketHandler handler, //
      Exception arg3) {

  }

}
```

### Add WebSocketConfig

First of all let us configure our message broker.In WebSocketConfig.java we have defined our STOMP endpoint and message broker. The important thing to notice here is the configuration for handshakeInterceptor.So, whenever any websocket handshake request is received by the server,this class will come into picture.

```java
package com.example.bootweb.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.example.bootweb.websocket.handler.WebSocketSessionHandShakeInterceptor;
import com.example.bootweb.websocket.profile.WebSocketSessionDemo;

@Configuration
@EnableWebSocketMessageBroker
@WebSocketSessionDemo
public class WebSocketSessionConfig extends AbstractWebSocketMessageBrokerConfigurer {

  // 作用是定义消息代理，通俗一点讲就是设置消息连接请求的各种规范信息。
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    // 一个定义了客户端接收的地址前缀，一个定义了客户端发送地址的前缀
    config.enableSimpleBroker("/topic/", "/queue/");// 表示客户端订阅地址的前缀信息，也就是客户端接收服务端消息的地址的前缀信息
    config.setApplicationDestinationPrefixes("/app");// 指服务端接收地址的前缀，意思就是说客户端给服务端发消息的地址的前缀
  }

  // 添加一个服务端点，来接收客户端的连接。
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/guide-websocket") // 表示添加了一个socket端点，客户端就可以通过这个端点来进行连接。
    .addInterceptors(new WebSocketSessionHandShakeInterceptor()) //   
    .withSockJS();// withSockJS()的作用是开启SockJS支持
  }

}
```

### Add SubscribeEventListener

Following is the listener class which will be executed whenever a STOMP client subscribes for a queue or topic. We are also validating the sessionId which we created during handshake.

```java
package com.example.bootweb.websocket.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.example.bootweb.websocket.profile.WebSocketSessionDemo;

@Component
@WebSocketSessionDemo
public class WebSocketSessionSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Override
  public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
    logger.info("SessionId: {}", headerAccessor.getSessionAttributes().get("sessionId").toString());
  }

}
```

### Add Controller

Now let us define the controller which will intercept the message coming from the stomp client and validate the session using custom attribute sessionId which we set during handshake.This attribute can be accessed from the headerAccessor.

```java
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

```

## Client Side

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Hello WebSocket Session</title>
  <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="/main.css" rel="stylesheet">
  <script src="/webjars/jquery/jquery.min.js"></script>
  <script src="/webjars/sockjs-client/sockjs.min.js"></script>
  <script src="/webjars/stomp-websocket/stomp.min.js"></script>
</head>
<body>
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
    enabled. Please enable
    Javascript and reload this page!</h2></noscript>
<div class="container">
  <div class="panel panel-default">
    <div class="panel-heading">
      <h3 class="panel-title">WebSocket Session Keeping Demo</h3>
    </div>
    <div class="panel-body">
      <div class="list-group">
        <div class="list-group-item">Run as spring.profiles.active=WebSocketSessionDemo</div>
        <a href="https://spring.io/guides/gs/messaging-stomp-websocket/" target="_blank" class="list-group-item">Using WebSocket to build an interactive web application</a>
        <div class="list-group-item">
          <div class="input-group">
            <span class="input-group-btn">
              <button class="btn btn-default" type="button" id="connect">Connect</button>
              <button class="btn btn-default" type="button" id="disconnect" disabled="disabled">Disconnect</button>
            </span>
            <input type="text" id="user" class="form-control" placeholder="The user of message to ">
          </div>
        </div>
        <div class="list-group-item">
          <div class="input-group">
            <input type="text" id="name" class="form-control" placeholder="Your message here...">
            <span class="input-group-btn">
              <button class="btn btn-default" type="button" id="send" disabled="disabled">Send</button>
            </span>
          </div>
        </div>
      </div>
      <div class="list-group server-message">
        <div class="list-group-item disabled">Server Message</div>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript">
//@WebSocketSessionDemo
var stompClient = null;

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	$("#send").prop("disabled", !connected);
}

function connect() {
	var socket = new SockJS('/websocket-server-endpoint');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/reply', function(greeting) {
		  console.log(greeting);
		  showGreeting(greeting.body);
		});
	});
}

function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function sendName() {
  if (!$("#name").val()) {
    return;
  }
  stompClient.send("/app/message", {}, JSON.stringify({
    'name' : $("#name").val(),
    'user' : $("#user").val()
  }));
  $("#name").val('');
}

function showGreeting(message) {
	$("div.server-message").append('<div class="list-group-item">' + message + '</div>');
}

$(function() {
	$("#connect").click(function() {
		connect();
	});
	$("#disconnect").click(function() {
		disconnect();
	});
	$("#send").click(function() {
		sendName();
	});
});
</script>
</body>
</html>
```
