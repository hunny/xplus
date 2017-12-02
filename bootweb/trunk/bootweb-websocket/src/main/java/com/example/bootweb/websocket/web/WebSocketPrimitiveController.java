package com.example.bootweb.websocket.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.websocket.handler.WebSocketPrimitiveServer;
import com.example.bootweb.websocket.profile.WebSocketPrimitiveDemo;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@WebSocketPrimitiveDemo
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
