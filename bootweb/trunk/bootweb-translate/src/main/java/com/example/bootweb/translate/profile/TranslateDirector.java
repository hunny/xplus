package com.example.bootweb.translate.profile;

import com.example.bootweb.translate.api.TranslateBuilder;

public class TranslateDirector {

  private TranslateBuilder translateBuilder;

  public void setTranslateBuilder(TranslateBuilder translateBuilder) {
    this.translateBuilder = translateBuilder;
  }
  
  public void build() {
    translateBuilder.build();
  }
  
}
