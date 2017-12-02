# Spring Boot中WebSocket的原始简单应用

* 在Spring Boot中使用WebSocket与其他方式会有所不同，主要还是因为Spring Boot会内置tomcat以及Spring Boot做了一定的配置。

## 演示应用目标

* 实现一个简单的消息功能
  - 多用户连接。
  - 客户端向服务端发送信息。
  - 服务端向客户端发送信息。

## 依赖

* 在`pom.xml`中引入依赖

```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>
```

* 如果是使用Spring Boot内置tomcat进行部署，pom只需要引入`spring-boot-starter-websocket`就可以了。
* 如果是使用war包方式到tomcat中进行部署，需要引入如下依赖：

```xml
    <dependency>
      <groupId>javax</groupId>
      <artifactId>javaee-api</artifactId>
      <version>7.0</version>
      <scope>provided</scope>
    </dependency>
```
  - 因为这里面带有javaee对websocket支持的标准规范（jsr356）的注解。主要是@ServerEndpoint。

## 配置

* 如果使用Spring Boot内置tomcat进行部署，在编写WebSocket具体实现类之前，要注入`ServerEndpointExporter`，这个bean会自动注册使用了`@ServerEndpoint`注解声明的Websocket Endpoint。

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketPrimitiveConfig {
  
  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }

}
```

* 如果使用war包部署到tomcat中进行部署，可以不做此操作，因为它将由容器自己提供和管理。

## WebSocket服务端具体实现类

```java
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

@ServerEndpoint(value = "/websocketServer/{userid}")
@Component
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
```

## 服务器交互实现

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SocketPrimitiveServer;
import WebSocketPrimitiveDemo;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WebSocketPrimitiveController {

  @GetMapping(value = "/send/{userid}")
  public ResponseEntity<String> sendMessage(@PathVariable("userid") String userId, //
      @RequestParam("message") String message //
  ) {
    WebSocketPrimitiveServer.sendMessage(userId, message);
    return new ResponseEntity<String>(HttpStatus.OK);
  }

  @GetMapping(value = "/send/to/all")
  public ResponseEntity<String> sendMessageToAll(@RequestParam("message") String message //
  ) {
    WebSocketPrimitiveServer.sendToAll(message);
    return new ResponseEntity<String>(HttpStatus.OK);
  }

}
```

## WebSocket客户端实现

```javascript
var ws = null;
function connect() {
  if (!$('#userId').val()) {
    console.log('请输入用户ID');
    return;
  }
  var wsUrl = 'ws://localhost:8080/websocketServer/' + $('#userId').val();
  if ('WebSocket' in window) {
	    console.log('StandardWebSocket');
  	  ws = new WebSocket(wsUrl);
  } else if ('MozWebSocket' in window) {
    console.log('MozWebSocket');
    ws = new MozWebSocket(wsUrl);
  } else {
    alert('该浏览器不支持WebSocket.');
    return;
  }
  ws.onmessage = function(resp) {
  	console.log('收到消息：' + resp.data);
  	$('#server-msg').append('<li class="list-group-item">' + resp.data + '</li>');
  }
  ws.onclose = function(resp) {
  	console.log('连接中断');
  	setConnected(false);
  }
  ws.onopen = function() {
  	console.log('连接成功');
  	setConnected(true);
  }
}
function disconnect() {
  if (ws != null) {
    	ws.close();
  }
  console.log("Disconnected");
  setConnected(false);
}

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
}

function send(userId, message) {
  if (!userId || !message) {
    console.log('UserId Or Message is blank. [' + userId + '], [' + message + ']');
    return;
  }
  $.ajax({
    url:"/send/" + userId,
    type: 'GET',
    data: {
      'message': message,
      '_': new Date().getTime()
    },
    success: function (result) {
      console.log(result);
    }, error: function (result) {
    	  console.log('OK');
  	}//
  });
}

function sendAll(message) {
  if (!message) {
    console.log('Message is blank. [' + message + ']');
    return;
  }
  $.ajax({
    url: "/send/to/all",
    type: 'GET',
    data: {
      'message': message,
      '_': new Date().getTime()
    },
    success: function(result) {
      console.log(result);
    }, error: function(result) {
    	  console.log('OK');
  	}//
  });
}
```

## 完整客户端代码

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>WebSocket - Primitive Demo</title>
  <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <script src="/webjars/jquery/jquery.min.js"></script>
</head>
<body>
<div id="main-content" class="container">
  <h2>WebSocket - Primitive Demo</h2>
  <div class="row">
    <div class="col-md-12">
      <div class="list-group">
        <div class="list-group-item">
          <div class="input-group">
            <span class="input-group-addon">UserId　@</span>
            <input type="text" id="userId" class="form-control" placeholder="UserId">
            <div class="input-group-btn">
              <button id="connect" class="btn btn-default" type="button">Connect</button>
              <button id="disconnect" class="btn btn-default" type="button" disabled="disabled">Disconnect
            </div>
          </div>
        </div>
        <div class="list-group-item">
          <div class="input-group">
            <span class="input-group-addon">Message@</span>
            <input type="text" id="message" class="form-control" placeholder="Message">
            <div class="input-group-btn">
              <button id="send" class="btn btn-default" type="button">Send</button>
            </div>
          </div>
        </div>
        <div class="list-group-item">
          <div class="input-group">
            <span class="input-group-addon">Message@</span>
            <input type="text" id="allmessage" class="form-control" placeholder="Message">
            <div class="input-group-btn">
              <button id="sendAll" class="btn btn-default" type="button">Send All</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-md-12">
      <ul id="server-msg" class="list-group">
        <li class="list-group-item disabled">服务端消息</li>
      </ul>
    </div>
  </div>
</div>
<script type="text/javascript">
var ws = null;
function connect() {
  if (!$('#userId').val()) {
    console.log('请输入用户ID');
    return;
  }
  var wsUrl = 'ws://localhost:8080/websocketServer/' + $('#userId').val();
  if ('WebSocket' in window) {
	    console.log('StandardWebSocket');
  	  ws = new WebSocket(wsUrl);
  } else if ('MozWebSocket' in window) {
    console.log('MozWebSocket');
    ws = new MozWebSocket(wsUrl);
  } else {
    alert('该浏览器不支持WebSocket.');
    return;
  }
  ws.onmessage = function(resp) {
  	console.log('收到消息：' + resp.data);
  	$('#server-msg').append('<li class="list-group-item">' + resp.data + '</li>');
  }
  ws.onclose = function(resp) {
  	console.log('连接中断');
  	setConnected(false);
  }
  ws.onopen = function() {
  	console.log('连接成功');
  	setConnected(true);
  }
}
function disconnect() {
  if (ws != null) {
    	ws.close();
  }
  console.log("Disconnected");
  setConnected(false);
}

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
}

function send(userId, message) {
  if (!userId || !message) {
    console.log('UserId Or Message is blank. [' + userId + '], [' + message + ']');
    return;
  }
  $.ajax({
    url:"/send/" + userId,
    type: 'GET',
    data: {
      'message': message,
      '_': new Date().getTime()
    },
    success: function (result) {
      console.log(result);
    }, error: function (result) {
    	  console.log('OK');
  	}//
  });
}

function sendAll(message) {
  if (!message) {
    console.log('Message is blank. [' + message + ']');
    return;
  }
  $.ajax({
    url: "/send/to/all",
    type: 'GET',
    data: {
      'message': message,
      '_': new Date().getTime()
    },
    success: function(result) {
      console.log(result);
    }, error: function(result) {
    	  console.log('OK');
  	}//
  });
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
  	send($('#userId').val(), $('#message').val());
  });
  $("#sendAll").click(function() {
  	sendAll($('#allmessage').val());
  });
});
</script>
</body>
</html>
```