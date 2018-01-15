package com.example.bootweb.translate.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.StringUtils;

public class UserAgent {

  public static final List<String> USER_AGENTS = new ArrayList<>();
  
  static {
    USER_AGENTS.add("\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36\"");
  }
  
  public static String get() {
    String userAgent = System.getProperty("user.agent");
    if (StringUtils.isEmpty(userAgent)) {
      Collections.shuffle(USER_AGENTS);
      userAgent = USER_AGENTS.get(0);
    }
    return userAgent;
  }
  
}
