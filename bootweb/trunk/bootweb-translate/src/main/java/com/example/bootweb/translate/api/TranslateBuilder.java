package com.example.bootweb.translate.api;

public interface TranslateBuilder {

  TranslateBuilder setTranslate(Translate translate);

  TranslateBuilder setFrom(Class<? extends Lang> from);

  TranslateBuilder setTo(Class<? extends Lang> to);

  TranslateBuilder setText(String text);

  Translate build();

}
