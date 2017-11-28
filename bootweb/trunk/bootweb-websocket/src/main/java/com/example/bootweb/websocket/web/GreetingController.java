package com.example.bootweb.websocket.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.bootweb.websocket.profile.WebSocketDemo;

@Controller
@WebSocketDemo
public class GreetingController {

  @MessageMapping("/hello") // 定义一个消息的基本请求
  @SendTo("/topic/greetings") // 发送消息到指定的目的地，定义了消息的目的地。结合例子解释就是“接收/app/hello发来的value，然后将value转发到/topic/greetings客户端（/topic/greetings是客户端发起连接后，订阅服务端消息时指定的一个地址，用于接收服务端的返回）。
  public Greeting greeting(HelloMessage message) throws Exception {
    Thread.sleep(1000); // simulated delay
    return new Greeting("Hello, " + message.getName() + "!");
  }

}
