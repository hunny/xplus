# Using a basic websockeet applicaiton

Start with this example, [https://spring.io/guides/gs/messaging-stomp-websocket/](https://spring.io/guides/gs/messaging-stomp-websocket/)

## Enable Websocket support, For example by using:

```java
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  public Greeting greeting(HelloMessage message) throws Exception {
    Thread.sleep(1000); // simulated delay
    return new Greeting("Hello, " + message.getName() + "!");
  }

}
```

* 使用`@SendTo`的方式发送消息，等同如下方式发送消息：

```java
  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;
  
  public String send(String message) {
    simpMessagingTemplate.convertAndSend("/topic/updateService", message);
    return message;
  }
```

## Second, Create a message-handling controller:

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/guide-websocket").withSockJS();
  }

}
```

## Create a browser client

```javascript
var stompClient = null;

function connect() {
	var socket = new SockJS('/guide-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/greetings', function(greeting) {
			showGreeting(JSON.parse(greeting.body).content);
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

```

## 使用`SockJS`&`Stomp`实现WebSocket

### 简介

* SockJS 是一个浏览器上运行的 JavaScript 库，如果浏览器不支持 WebSocket，该库可以模拟对 WebSocket 的支持，实现浏览器和 Web 服务器之间低延迟、全双工、跨域的通讯通道。

* Stomp 提供了客户端和代理之间进行广泛消息传输的框架。Stomp 是一个非常简单而且易用的通讯协议实现，尽管代理端的编写可能非常复杂，但是编写一个 Stomp 客户端却是很简单的事情，另外可以使用 Telnet 来与你的 Stomp 代理进行交互。

### 实现

* 开启Socket

```javascript
var socket = new SockJS('/socket'); // 先构建一个SockJS对象
stompClient = Stomp.over(socket); // 用Stomp将SockJS进行协议封装
stompClient.connect(); // 与服务端进行连接，同时有一个回调函数，处理连接成功后的操作信息。
```

* 发送消息

```javascript
stompClient.send("/app/hello", {}, JSON.stringify({
	'name' : 'Hello World.'
}));
```

* 接收消息

```javascript
stompClient.subscribe('/topic/greetings', function(greeting) {
	showGreeting(JSON.parse(greeting.body).content);
});
```

