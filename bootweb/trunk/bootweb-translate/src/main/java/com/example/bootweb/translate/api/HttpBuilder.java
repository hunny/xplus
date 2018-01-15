package com.example.bootweb.translate.api;

import java.util.List;

public interface HttpBuilder<S, T> {

  public static final String USER_AGENT = "User-Agent";
  public static final String REFERER = "Referer";
  
  HttpBuilder<S, T> params(List<Param> params);
  
  HttpBuilder<S, T> parser(Parser<S, T> parser);
  
  HttpBuilder<S, T> addHeader(String key, String name);
  
  HttpBuilder<S, T> uri(String uri);
  
  String build();
  
}
