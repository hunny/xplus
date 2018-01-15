package com.example.bootweb.translate.api;

public interface TranslateBuilder<S, T> {

  TranslateBuilder from(Class<? extends Lang> from);

  TranslateBuilder to(Class<? extends Lang> to);

  TranslateBuilder source(S s);

  T build();

}
