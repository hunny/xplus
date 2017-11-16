package com.example.bootweb.angularjs.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ibm.icu.text.MessageFormat;

@Controller
@RequestMapping("/sse")
public class ServerSendEventsController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @RequestMapping(value = "/show.html", //
      method = RequestMethod.GET) //
  public String showhtml(HttpServletResponse response) throws IOException {
    logger.info("收到请求Server Send Events请求。");
//    response.setHeader("Content-type", "text/event-stream;charset=UTF-8"); 
    response.setHeader("Content-type", "text/event-stream"); 
    response.setCharacterEncoding("UTF-8"); 
    String message = MessageFormat.format("Server Send Events: {0}", new Date());
  //媒体类型为 text/event-stream
    response.setContentType("text/event-stream");
    response.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();

    //响应报文格式为:
    //data:Hello World
    //event:load
    //id:140312
    //换行符(/r/n)

    out.println("data:" + message);
    out.println("event:load");
    out.println("id:" + new Random().nextLong());
    out.println();
    out.flush();
    out.close();
    return message;
  }

}
