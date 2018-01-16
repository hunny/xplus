package com.example.bootweb.translate.api;

import java.util.List;

public interface HttpBuilder<S, T> {

  public static final String USER_AGENT = "User-Agent";
  public static final String REFERER = "Referer";
  
  HttpBuilder<S, T> params(List<Param> params);
  
  @SuppressWarnings("rawtypes")
  <P extends Parser> HttpBuilder<S, T> parser(P parser);
  
  HttpBuilder<S, T> addHeader(String key, String name);
  
  HttpBuilder<S, T> uri(String uri);
  
  T build();
  
}
