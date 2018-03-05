package com.example.bootweb.accessory.builder;

import java.util.ArrayList;
import java.util.List;

import com.example.bootweb.accessory.api.HeaderBuilder;
import com.example.bootweb.accessory.api.Param;

public class MobileHeaderBuilder implements HeaderBuilder {

  private List<Param> list = new ArrayList<>();

  private MobileHeaderBuilder() {
    list.add(new Param("Upgrade-Insecure-Requests", "1"));
    list.add(new Param("User-Agent",
        "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Mobile Safari/537.36"));
  }
  
  public static MobileHeaderBuilder newBuilder() {
    return new MobileHeaderBuilder();
  }

  @Override
  public List<Param> build() {
    return list;
  }

}
