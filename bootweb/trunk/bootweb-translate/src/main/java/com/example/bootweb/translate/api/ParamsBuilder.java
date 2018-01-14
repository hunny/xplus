package com.example.bootweb.translate.api;

import java.util.List;

public interface ParamsBuilder {
  
  ParamsBuilder setText(String text);
  
  List<Params> build();
  
}
