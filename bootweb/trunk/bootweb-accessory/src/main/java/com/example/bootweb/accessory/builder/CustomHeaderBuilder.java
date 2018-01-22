package com.example.bootweb.accessory.builder;

import java.util.ArrayList;
import java.util.List;

import com.example.bootweb.accessory.api.HeaderBuilder;
import com.example.bootweb.accessory.api.Param;

public class CustomHeaderBuilder implements HeaderBuilder {

  private List<Param> list = new ArrayList<>();

  private CustomHeaderBuilder() {
    list.add(new Param("Accept",
        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
    list.add(new Param("Accept-Encoding", "gzip, deflate, br"));
    list.add(new Param("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-TW;q=0.6"));
    list.add(new Param("Connection", "keep-alive"));
    list.add(new Param("User-Agent",
        "ozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Mobile Safari/537.36"));
  }
  
  public CustomHeaderBuilder newBuilder() {
    return new CustomHeaderBuilder();
  }

  @Override
  public List<Param> build() {
    return list;
  }

}
