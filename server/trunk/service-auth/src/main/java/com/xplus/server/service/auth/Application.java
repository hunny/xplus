package com.xplus.server.service.auth;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController // @RestController注解相当于@ResponseBody和@Controller合在一起的作用。
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @RequestMapping(value = {
      "/hi", "hello" }, //
      produces = "application/json;charset=UTF-8")
  public String hello() {
    return "Hello!";
  }

  @RequestMapping(value = {
      "/world", "/wd" }, //
      produces = "application/json;charset=UTF-8")
  public String world() {
    return "World";
  }

  // 路由映射到/users
  @RequestMapping(value = "/users", //
      produces = "application/json;charset=UTF-8")
  public String users() {
    List<String> users = new ArrayList<String>() {
      private static final long serialVersionUID = 9047104758343011245L;
      {
        add("Jack");
        add("Tom");
        add("Jerry");
      }
    };
    return StringUtils.join(users, ',');
  }

}
