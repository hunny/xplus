package com.example.bootweb.translate.api;

public interface TranslateBuilder<S, T> {

  TranslateBuilder<S, T> from(Class<? extends Lang> from);

  TranslateBuilder<S, T> to(Class<? extends Lang> to);

  TranslateBuilder<S, T> source(S s);

  T build();

}
