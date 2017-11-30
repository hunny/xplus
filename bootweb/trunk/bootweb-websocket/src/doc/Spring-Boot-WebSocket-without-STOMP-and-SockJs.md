# Spring Boot + WebSocket example without STOMP and SockJs

## STOMP

STOMP stands for Streaming Text Oriented Messaging Protocol. As per wiki, STOMP is a simple text-based protocol, designed for working with message-oriented middleware (MOM). It provides an interoperable wire format that allows STOMP clients to talk with any message broker supporting the protocol.

This means when we do not have STOMP as a client, the message sent lacks of information to make Spring route it to a specific message handler method. So, if we could create a mechanism that can make Spring to route to a specific message handler then probably we can make websocket connection without STOMP.

## Custom WebSocketHandler

We concluded that if we do not want to use STOMP, then we have to define our custom message protocol handler that should handle all the messages coming from any websocket client. Before defining any custom message handler class, we need to identify what format of message our handler class expect from the client. It can be either XML or Json or of any other format.

In this example we assume that the websocket client will be communicating with the server in string. Hence our handler will be a JSON message handler and we can define this in Spring by extending TextWebSocketHandler.java


## TextWebSocketHandler

TextWebSocketHandler is a class in Spring library that extends AbstractWebSockethandler class and primarily meant for handling only text messages. So, if you are expecting any binary messages from your websocket client then defining your custom handler class by extending TextWebSocketHandler is of no use as binary messages will be rejected with CloseStatus.NOT_ACCEPTABLE.

## Implementation of Spring Websocket without STOMP

While coming to the code implementation, we will be creating a simple web app using Spring websocket where message transfer between the client and server will happen via websocket without STOMP. 

### Step.1 Maven Dependency

```xml

  <parent>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-parent</artifactId>
	<version>1.5.4.RELEASE</version>
  </parent>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
```

### Step.2 Application

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
  
}
```

### Step.3 Overriding TextWebSocketHandler

Now let us define our custom handler class by extending TextWebSocketHandler.java that will override the protocol to handle the text message. Our handler will be capable of handling Json message. This class records all the session once any websocket connection is established and broadcasts the message to all the sessions once any message is received.

Instead of broadcasting message to all the clients, you can also configure to send message to a particular session. This configuration is also explained in coming section.

```java
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketWithoutSTOMPSockJsSocketHandler extends TextWebSocketHandler {
  
  private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
  
  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message)
      throws InterruptedException, IOException {
    for(WebSocketSession webSocketSession : sessions) {
      String str = message.getPayload();
      if (webSocketSession.isOpen()) {
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
    sessions.remove(session);//after closed will remove session from sessions.
  }
  
}
```

### Step.4 Spring Web Socket Configurations

Now let us define our websocket configurations. The below configuration will register websocket handler at `/name` and define our custom handler class that will handle all the messages from the websocket client. Attention `@EnableWebSocket`, `@Configuration` and `implements WebSocketConfigurer`.

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.bootweb.websocket.handler.WebSocketWithoutSTOMPSockJsSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketWithoutSTOMPSockJsConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new WebSocketWithoutSTOMPSockJsSocketHandler(), "/name");
  }

}
```

This is it for server side. 

### Implemention of client side in javascript

```javascript
var ws = null;
function connect() {
	ws = new WebSocket('ws://localhost:8080/name');
	ws.onmessage = function(data){
		showMessage(data.data);
	}
	setConnected(true);
}
function disconnect() {
    if (ws != null) {
        ws.close();
    }
    setConnected(false);
    console.log("Disconnected");
}

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
}

function showMessage(message) {
    $("#server-msg").append('<li class="list-group-item">' + message + '</li>');
}

function sendName() {
    ws.send($("#name").val());
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
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
```
